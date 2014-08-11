package com.sirius.upns.server.console.sitemesh;

import com.opensymphony.module.sitemesh.Config;
import com.opensymphony.module.sitemesh.factory.BaseFactory;
import com.opensymphony.module.sitemesh.factory.FactoryException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by pippo on 14-3-23.
 */
public class ClassPathConfigFactory extends BaseFactory {

//    private static final Logger LOGGER = LoggerFactory.getLogger(ClassPathConfigFactory.class);

    private static final String DEFAULT_CONFIG_FILENAME = "/META-INF/sitemesh.xml";

    private volatile boolean inited = false;

    private Map<String,Object> configProps = new HashMap<String,Object>();

    public ClassPathConfigFactory(Config config) {
        super(config);
    }

    /** Load configuration from file. */
    private synchronized void loadConfig() {
        try {
            // Load and parse the sitemesh.xml file
            Element root = loadSitemeshXML();

            NodeList sections = root.getChildNodes();
            // Loop through child elements of root node
            for (int i = 0; i < sections.getLength(); i++) {
                if (sections.item(i) instanceof Element) {
                    Element curr = (Element) sections.item(i);
                    NodeList children = curr.getChildNodes();

                    if ("config-refresh".equalsIgnoreCase(curr.getTagName())) {

                    } else if ("property".equalsIgnoreCase(curr.getTagName())) {
                        String name = curr.getAttribute("name");
                        String value = curr.getAttribute("value");
                        if (!"".equals(name) && !"".equals(value)) {
                            configProps.put("${" + name + "}", value);
                        }
                    } else if ("page-parsers".equalsIgnoreCase(curr.getTagName())) {
                        // handle <page-parsers>
                        loadPageParsers(children);
                    } else if ("decorator-mappers".equalsIgnoreCase(curr.getTagName())) {
                        // handle <decorator-mappers>
                        loadDecoratorMappers(children);
                    } else if ("excludes".equalsIgnoreCase(curr.getTagName())) {
                        // handle <excludes>
                        String fileName = replaceProperties(curr.getAttribute("file"));
                        if (!"".equals(fileName)) {
                            loadExcludes();
                        }
                    }
                }
            }

            inited = true;
        } catch (ParserConfigurationException e) {
            throw new FactoryException("Could not get XML parser", e);
        } catch (IOException e) {
            throw new FactoryException("Could not read config file : " + DEFAULT_CONFIG_FILENAME, e);
        } catch (SAXException e) {
            throw new FactoryException("Could not parse config file : " + DEFAULT_CONFIG_FILENAME, e);
        }
    }

    private Element loadSitemeshXML()
            throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        InputStream is = ClassPathConfigFactory.class.getResourceAsStream(DEFAULT_CONFIG_FILENAME);

        if (is == null) {
            throw new IllegalStateException("Cannot load default configuration from jar");
        }

        Document doc = builder.parse(is);
        Element root = doc.getDocumentElement();
        // Verify root element
        if (!"sitemesh".equalsIgnoreCase(root.getTagName())) {
            throw new FactoryException("Root element of sitemesh configuration file not <sitemesh>", null);
        }
        return root;
    }

    private void loadExcludes()
            throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        InputStream is = ClassPathConfigFactory.class.getResourceAsStream(DEFAULT_CONFIG_FILENAME);

        if (is == null) {
            throw new IllegalStateException("Cannot load excludes configuration file " + DEFAULT_CONFIG_FILENAME);
        }

        Document document = builder.parse(is);
        Element root = document.getDocumentElement();
        NodeList sections = root.getChildNodes();

        // Loop through child elements of root node looking for the <excludes> block
        for (int i = 0; i < sections.getLength(); i++) {
            if (sections.item(i) instanceof Element) {
                Element curr = (Element) sections.item(i);
                if ("excludes".equalsIgnoreCase(curr.getTagName())) {
                    loadExcludeUrls(curr.getChildNodes());
                }
            }
        }
    }

    /** Loop through children of 'page-parsers' element and add all 'parser' mappings. */
    private void loadPageParsers(NodeList nodes) {
        clearParserMappings();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i) instanceof Element) {
                Element curr = (Element) nodes.item(i);

                if ("parser".equalsIgnoreCase(curr.getTagName())) {
                    String className = curr.getAttribute("class");
                    String contentType = curr.getAttribute("content-type");
                    mapParser(contentType, className);
                }
            }
        }
    }

    private void loadDecoratorMappers(NodeList nodes) {
        clearDecoratorMappers();
        Properties emptyProps = new Properties();

        pushDecoratorMapper("com.opensymphony.module.sitemesh.mapper.NullDecoratorMapper", emptyProps);

        // note, this works from the bottom node up.
        for (int i = nodes.getLength() - 1; i > 0; i--) {
            if (nodes.item(i) instanceof Element) {
                Element curr = (Element) nodes.item(i);
                if ("mapper".equalsIgnoreCase(curr.getTagName())) {
                    String className = curr.getAttribute("class");
                    Properties props = new Properties();
                    // build properties from <param> tags.
                    NodeList children = curr.getChildNodes();
                    for (int j = 0; j < children.getLength(); j++) {
                        if (children.item(j) instanceof Element) {
                            Element currC = (Element) children.item(j);
                            if ("param".equalsIgnoreCase(currC.getTagName())) {
                                String value = currC.getAttribute("value");
                                props.put(currC.getAttribute("name"), replaceProperties(value));
                            }
                        }
                    }
                    // add mapper
                    pushDecoratorMapper(className, props);
                }
            }
        }

        pushDecoratorMapper("com.opensymphony.module.sitemesh.mapper.InlineDecoratorMapper", emptyProps);
    }

    /**
     * Reads in all the url patterns to exclude from decoration.
     */
    private void loadExcludeUrls(NodeList nodes) {
        clearExcludeUrls();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i) instanceof Element) {
                Element p = (Element) nodes.item(i);
                if ("pattern".equalsIgnoreCase(p.getTagName()) || "url-pattern".equalsIgnoreCase(p.getTagName())) {
                    Text patternText = (Text) p.getFirstChild();
                    if (patternText != null) {
                        String pattern = patternText.getData().trim();
                        if (pattern != null) {
                            addExcludeUrl(pattern);
                        }
                    }
                }
            }
        }
    }

    /** Check if configuration file has been modified, and if so reload it. */
    public void refresh() {
        if (!inited) {
            loadConfig();
        }
    }

    /**
     * Replaces any properties that appear in the supplied string
     * with their actual values
     *
     * @param str the string to replace the properties in
     * @return the same string but with any properties expanded out to their
     * actual values
     */
    @SuppressWarnings("rawtypes")
	private String replaceProperties(String str) {
        Set props = configProps.entrySet();
        for (Iterator it = props.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            int idx;
            while ((idx = str.indexOf(key)) >= 0) {
                StringBuffer buf = new StringBuffer(100);
                buf.append(str.substring(0, idx));
                buf.append(entry.getValue());
                buf.append(str.substring(idx + key.length()));
                str = buf.toString();
            }
        }
        return str;
    }
}