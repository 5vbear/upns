package com.sirius.upns.server.console.sitemesh;

import com.opensymphony.module.sitemesh.Config;
import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.DecoratorMapper;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.mapper.AbstractDecoratorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

/**
 * Created by pippo on 14-3-23.
 */
public class ExtConfigDecoratorMapper extends AbstractDecoratorMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtConfigDecoratorMapper.class);

    private ClassPathConfigLoader configLoader = null;

    /** Create new ConfigLoader using '/META-INF/decorators.xml' file. */
    public void init(Config config, Properties properties, DecoratorMapper parent) throws InstantiationException {
        super.init(config, properties, parent);
        try {
            String decorators = ExtConfigDecoratorMapper.class.getResource("/META-INF/decorators.xml").getFile();
            LOGGER.info("the decorators file is:[{}]", decorators);
            configLoader = new ClassPathConfigLoader("/META-INF/decorators.xml", config);
        } catch (Exception e) {
            throw new InstantiationException(e.toString());
        }
    }

    /** Retrieve {@link com.opensymphony.module.sitemesh.Decorator} based on 'pattern' tag. */
    public Decorator getDecorator(HttpServletRequest request, Page page) {
        String thisPath = request.getServletPath();

        // getServletPath() returns null unless the mapping corresponds to a servlet
        if (thisPath == null) {
            String requestURI = request.getRequestURI();
            if (request.getPathInfo() != null) {
                // strip the pathInfo from the requestURI
                thisPath = requestURI.substring(0, requestURI.indexOf(request.getPathInfo()));
            } else {
                thisPath = requestURI;
            }
        } else if ("".equals(thisPath)) {
            // in servlet 2.4, if a request is mapped to '/*', getServletPath returns null (SIM-130)
            thisPath = request.getPathInfo();
        }

        String name = null;
        try {
            name = configLoader.getMappedName(thisPath);
        } catch (ServletException e) {
            e.printStackTrace();
        }

        Decorator result = getNamedDecorator(request, name);
        return result == null ? super.getDecorator(request, page) : result;
    }

    /** Retrieve Decorator named in 'name' attribute. Checks the role if specified. */
    public Decorator getNamedDecorator(HttpServletRequest request, String name) {
        Decorator result = null;
        try {
            result = configLoader.getDecoratorByName(name);
        } catch (ServletException e) {
            e.printStackTrace();
        }

        if (result == null || (result.getRole() != null && !request.isUserInRole(result.getRole()))) {
            // if the result is null or the user is not in the role
            return super.getNamedDecorator(request, name);
        } else {
            return result;
        }
    }
}
