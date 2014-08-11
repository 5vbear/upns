/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.repository.impl;

import com.mongodb.DB;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.FactoryBean;

/**
 * @project node-server
 * @date 2013-9-9-上午12:18:18
 * @author pippo
 */
public class JongoFactoryBean implements FactoryBean<MongoCollection> {

	private static Jongo jongo;

	private MongoCollection mongoCollection;

	@Override
	public MongoCollection getObject() throws Exception {
		return mongoCollection;
	}

	@Override
	public Class<?> getObjectType() {
		return MongoCollection.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void init() {
		if (jongo == null) {
			jongo = new Jongo(db);
		}

		mongoCollection = jongo.getCollection(collection);
	}

	private DB db;

	public void setDb(DB db) {
		this.db = db;
	}

	private String collection;

	public void setCollection(String collection) {
		this.collection = collection;
	}

}
