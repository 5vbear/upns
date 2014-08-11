/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.upns.server.widget.websocket;

import com.sirius.upns.protocol.business.BusinessPacket;
import com.sirius.upns.protocol.business.DeviceType;
import com.sirius.upns.protocol.business.msg.APPUnread;
import com.sirius.upns.server.node.domain.Subscriber;
import com.sirius.upns.server.node.service.SubscriberManager;
import com.sirius.upns.server.node.service.TimelineService;
import com.sirius.upns.server.widget.websocket.endpoint.MultiplexEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.websocket.Session;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: pippo
 * Date: 14-2-24-12:53
 */
@Component
public class SessionManager {

	private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);

	private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

	@Resource(name = "ctu.upns.subscriberManager")
	private SubscriberManager subscriberManager;

	@Resource(name = "upns.timelineService")
	private TimelineService timelineService;

	@PostConstruct
	public void init() {
		MultiplexEndpoint.setSessionManager(this);
	}

	public void onOpen(final Session session, String userId) {
		/*为每一个websocket session建立一个subscriber*/
		Subscriber subscriber = subscriberManager.getById(session.getId());

		if (subscriber == null) {
			final WebSocketSubscriber _subscriber = new WebSocketSubscriber(session.getId(),
					userId,
					"websocket",
					DeviceType.web);
			_subscriber.setSession(session);
			_subscriber.start();
			logger.debug("create websocket subscriber:[{}]", _subscriber);

			/*第一次建立连接时下方unread*/
			EXECUTOR_SERVICE.submit(new Runnable() {
				@Override public void run() {
					List<APPUnread> appUnreads = timelineService.unread(_subscriber.getUserId());
                    _subscriber.publish(new BundleAPPUnread(appUnreads));

				}
			});
		}

		logger.debug("user:[{}] connect with session:[{}]", userId, session.getId());
	}


    public static class BundleAPPUnread implements BusinessPacket{

        private List<APPUnread> unreads;

        private BundleAPPUnread(List<APPUnread> unreads) {
            this.unreads = unreads;
        }

        @Override
        public Map<String, Object> toMap() {
            return new HashMap<String,Object>(){{
                put("unreads",unreads);
            }};
        }

        @Override
        public void fromMap(Map<String, Object> in) {
            this.unreads = (List<APPUnread>) in.get("unreads");
        }

        public List<APPUnread> getUnreads() {
            return unreads;
        }

        public void setUnreads(List<APPUnread> unreads) {
            this.unreads = unreads;
        }
    }

	public void onClose(Session session) {
		WebSocketSubscriber subscriber = (WebSocketSubscriber) subscriberManager.getById(session.getId());
		if (subscriber == null) {
			logger.warn("can not find subscriber with sesion:[{}]", session);
			return;
		}

        /*关闭针听*/
		subscriber.stop();
		logger.debug("user:[{}] disconnect with session:[{}]", subscriber.getUserId(), session.getId());
	}

}
