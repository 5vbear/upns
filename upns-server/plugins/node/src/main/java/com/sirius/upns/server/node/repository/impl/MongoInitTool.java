package com.sirius.upns.server.node.repository.impl;

import org.jongo.MongoCollection;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by pippo on 14-8-11.
 */
@Repository("upns.mongoInitTool")
public class MongoInitTool {

	@Resource(name = "ctu.upns.mongo.db.messages")
	protected MongoCollection messages;

	@Resource(name = "ctu.upns.mongo.db.acks")
	protected MongoCollection acks;

	@Resource(name = "ctu.upns.mongo.db.timelines")
	protected MongoCollection timelines;

	public void clearMessage() {
		messages.remove();
	}

	public void clearACK() {
		acks.remove();
	}

	public void clearTimeline() {
		timelines.remove();
	}
}
