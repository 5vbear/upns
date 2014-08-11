/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.service.impl;

import com.myctu.platform.gateway.agent.pubsub.PubSubException;
import com.myctu.platform.gateway.agent.pubsub.SubService;
import com.sirius.upns.protocol.KeyHelper;
import com.sirius.upns.server.node.domain.Subscriber;
import com.sirius.upns.server.node.service.SubscriberManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author pippo
 * @project node-server
 * @date 2013-8-26-下午6:03:13
 */
public class MemSubscriberManager implements SubscriberManager {

	private static final Logger logger = LoggerFactory.getLogger(MemSubscriberManager.class);

	/*subscriberId=Subscriber*/
	private static final Map<String, Subscriber> subscribers = new ConcurrentSkipListMap<>();

	/*userId=[subscriberId(phone1), subscriberId(phone2), ... ]*/
	private static final Map<String, Set<String>> users = new ConcurrentSkipListMap<String, Set<String>>() {

		@Override
		public Set<String> get(Object key) {
			Set<String> rtvl = super.get(key);
			if (rtvl == null) {
				rtvl = new HashSet<>();
				super.put((String) key, rtvl);
			}
			return rtvl;
		}

	};

	@Resource(name = "ctu.upns.subService")
	private SubService subService;

	@PostConstruct
	public void init() throws PubSubException {
		/*接收发送到用户的消息,并根据userId找到对应的subscriber直接发送私信*/
		subService.sub(KeyHelper.CHANNEL_PRIVATE, new PrivateChannelListener(this));
		/*接收所有广播消息,发送到连接上的终端*/
		subService.sub(KeyHelper.CHANNEL_BROADCAST, new BroadcastChannelListener(this));
	}

	@Override
	public void add(Subscriber subscriber) {
		if (logger.isTraceEnabled()) {
			logger.trace("add new subscriber:[{}], the subscribers is:[{}]",
					subscriber,
					Arrays.toString(subscribers.values().toArray()));
		}

        /*建立subscriberId和subscriber的映射关系*/
		subscribers.put(subscriber.getId(), subscriber);
		/*建立userId和subscriberId的映射关系*/
		users.get(subscriber.getUserId()).add(subscriber.getId());
	}

	@Override
	public void remove(String id) {
		Subscriber subscriber = subscribers.remove(id);
		if (subscriber != null) {
			String userId = subscriber.getUserId();
            /*获取一个用户所有的subscriber*/
			Set<String> _subscriber = users.get(userId);
            /*去除要remove的subscriber*/
			_subscriber.remove(subscriber.getId());
            /*如果一个用户的subscriber集合为空,那么去除映射关系,节省内存*/
			if (_subscriber.isEmpty()) {
				users.remove(userId);
			}
		}
	}

	@Override
	public Subscriber getById(String id) {
		return subscribers.get(id);
	}

	@Cacheable(value = "ctu.application.method.cache", key = "'subscribers'+#root.args[0]", unless = "#result==null")
	@Override
	public Set<Subscriber> getByUserId(String userId) {
		Set<Subscriber> _subscribers = new HashSet<>();
		for (String subscriberId : users.get(userId)) {
			_subscribers.add(subscribers.get(subscriberId));
		}
		return _subscribers;
	}

	@Override
	public void start(final Subscriber subscriber) {
		this.add(subscriber);

		//        try {
		//            subService.sub(KeyHelper.CHANNEL_BROADCAST, subscriber);
		//        } catch (PubSubException e) {
		//            logger.error("sub broadcast channel due to error:[{}]", ExceptionUtils.getStackTrace(e));
		//        }

		logger.debug("started subscriber:[{}]", subscriber);
	}

	@CacheEvict(value = "ctu.application.method.cache", key = "'subscribers'+#root.args[0].userId")
	@Override
	public void stop(final Subscriber subscriber) {
		this.remove(subscriber.getId());

		//        try {
		//            subService.unsub(KeyHelper.CHANNEL_BROADCAST, subscriber);
		//        } catch (PubSubException e) {
		//            logger.error("unsub broadcast channel due to error:[{}]", ExceptionUtils.getStackTrace(e));
		//        }

		logger.debug("stoped subscriber:[{}]", subscriber);
	}

	@Override
	public void iterate(SubscriberClosure closure) {
		for (String id : subscribers.keySet()) {
			Subscriber subscriber = subscribers.get(id);
			if (subscriber != null && subscriber.isValid()) {
				closure.execute(subscriber);
			}
		}
	}
}
