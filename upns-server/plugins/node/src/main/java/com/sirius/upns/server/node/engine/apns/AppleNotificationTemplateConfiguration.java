package com.sirius.upns.server.node.engine.apns;

import com.myctu.platform.AppSettings;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.AppleNotificationServer;
import javapns.notification.AppleNotificationServerBasicImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pippo on 14-5-13.
 */
@Configuration
public class AppleNotificationTemplateConfiguration {

	public static final String CTU_UPNS_SERVER_NODE_APNS_KEYSTORE = "ctu.upns.server.node.apns.key.store.";

	@Bean(name = "ctu.upns.apnsTemplate")
	public Templates templates() throws KeystoreException {
		Templates templates = new Templates();
		String[] keystores = AppSettings.getInstance().getByPrefix(CTU_UPNS_SERVER_NODE_APNS_KEYSTORE);

		/*单个的keystore格式为path#password*/
		for (String keystore : keystores) {
			String[] info = StringUtils.split(keystore,"#");
			String path = info[0];
			String password = info[1];
			URL keystorePath = AppleNotificationTemplateConfiguration.class.getResource("/" + path);
			Validate.notNull(keystorePath, "can not find keystore with path:[%s]", keystorePath);

			AppleNotificationServer server = new AppleNotificationServerBasicImpl(keystorePath.getFile(),
					password,
					type,
					host,
					port
			);

			PushNotificationTemplate template = new PushNotificationTemplate(server);

			/*keystore的名字必须是appid.p12*/
			String app = path.replace(".p12", "");
			templates.payloads.put(Integer.parseInt(app), template);
		}

		return templates;
	}

	private String type = "PKCS12";

	@Value("${ctu.upns.server.node.apns.host}")
	private String host;

	@Value("${ctu.upns.server.node.apns.port}")
	private int port;

	public static class Templates {

		/*{appId=PushNotificationTemplate}*/
		public Map<Integer, PushNotificationTemplate> payloads = new HashMap<>();

	}

}
