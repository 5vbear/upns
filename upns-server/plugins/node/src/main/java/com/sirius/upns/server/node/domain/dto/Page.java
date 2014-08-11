/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.domain.dto;

import java.io.Serializable;

/**
 * @project ctu-framework
 * @date 2013-9-12-下午1:27:57
 * @author pippo
 */
public class Page implements Serializable {

	private static final long serialVersionUID = 1851805633224790582L;

	public static final int _DEFAULT_LIMIT = 10;

	public static final Page _DEFAULT = new Page(0, _DEFAULT_LIMIT, -1);

	public Page() {
		this(0, _DEFAULT_LIMIT, -1);
	}

	public Page(int start, int limit) {
		this(start, limit, -1);
	}

	public Page(int start, int limit, int count) {
		this.start = Math.max(start, 0);
		this.limit = Math.min(limit, 1000);
		this.count = count;
	}

	public Page first() {
		this.start = 0;
		return this;
	}

	/* 方便分页,但会改变当前page状态 */
	public Page next() {
		if (hasNext()) {
			this.start = Math.min(start + limit, count);
		}

		return this;
	}

	public boolean hasNext() {
		return (start + limit) < count;
	}

	/* 方便分页,但会改变当前page状态 */
	public Page previous() {
		this.start = Math.max(start - limit, 0);
		return this;
	}

	/* 方便分页,会返回新的page对象,不改变当前page状态 */
	public Page getNext() {
		return start >= (count - limit) ? this : new Page(start + limit, limit, count);
	}

	public Page getPrevious() {
		return new Page(Math.max(start - limit, 0), limit, count);
	}

	public int getNumber() {
		if (start < limit) {
			return 1;
		}

		int number = start / limit + 1;
		int size = getSize();
		return number < size ? number : size;
	}

	public int getSize() {
		if (count == 0 || count < limit) {
			return 1;
		}

		return count / limit + (count % limit == 0 ? 0 : 1);
	}

	public int start;

	public int limit;

	public int count;

	public int getStart() {
		return start;
	}

	public Page setStart(int start) {
		this.start = start;
		return this;
	}

	public int getLimit() {
		return limit;
	}

	public Page setLimit(int limit) {
		this.limit = limit;
		return this;
	}

	public int getCount() {
		return count;
	}

	public Page setCount(int count) {
		this.count = count;
		return this;
	}

	@Override
	public String toString() {
		return String.format("Page [start=%s, limit=%s, count=%s, number()=%s, size()=%s]",
			start,
			limit,
			count,
			getNumber(),
			getSize());
	}

}
