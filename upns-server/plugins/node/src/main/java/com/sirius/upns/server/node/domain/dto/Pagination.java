/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.domain.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @project node-server
 * @date 2013-9-12-下午1:16:46
 * @author pippo
 */
public class Pagination<T> implements Serializable {

	private static final long serialVersionUID = 9207085961717730449L;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Pagination empty = new Pagination(Collections.emptyList(), new Page(0, 10, 0));

	@SuppressWarnings("unchecked")
	public static <T> Pagination<T> empty() {
		return (Pagination<T>) empty;
	}

	public Pagination(List<T> items, Page page) {
		this.items = items;
		this.page = page;
	}

	public final List<T> items;

	public final Page page;

	public List<T> getItems() {
		return items;
	}

	public Page getPage() {
		return page;
	}

	@Override
	public String toString() {
		return String.format("Pagination [items=%s, page=%s]", items, page);
	}

}
