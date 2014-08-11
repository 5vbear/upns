/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.domain.dto;

import com.myctu.platform.utils.BeanUtils;
import com.sirius.upns.server.node.domain.model.AbstractEntity;
import com.sirius.upns.server.node.repository.SearcherProcessor;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @project node-server
 * @date 2013-9-23-下午12:48:17
 * @author pippo
 */
public abstract class AbstractSearcher<E extends AbstractEntity> implements Searcher<E> {

	private static final Logger logger = LoggerFactory.getLogger(AbstractSearcher.class);

	public AbstractSearcher() {
		init();
	}

	protected E example;

	public E getExample() {
		return example;
	}

	public void setExample(E example) {
		this.example = example;
	}

	protected Page page;

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getURLAppender() {
		return String.format("page.start=%s&page.limit=%s&page.count=%s&%s",
			page.start,
			page.limit,
			page.count,
			urlAppender());
	}

	public String getNextURLAppender() {
		Page next = page.getNext();
		return String.format("page.start=%s&page.limit=%s&page.count=%s&%s",
			next.start,
			next.limit,
			next.count,
			urlAppender());
	}

	public String getPreviousURLAppender() {
		Page previous = page.getPrevious();
		return String.format("page.start=%s&page.limit=%s&page.count=%s&%s",
			previous.start,
			previous.limit,
			previous.count,
			urlAppender());
	}

	protected StringBuilder urlAppender() {
		if (example == null) {
			return new StringBuilder();
		}

		StringBuilder appender = new StringBuilder();

		Field[] fields = BeanUtils.getDeclaredFields(example.getClass());
		for (Field field : fields) {

			if (Modifier.isStatic(field.getModifiers())) {
				continue;
			}

			Object value = null;

			try {
				value = field.get(example);
			} catch (Exception e) {
				logger.warn("get field value due to error", e);
				continue;
			}

			if (value == null) {
				continue;
			}

			if ((field.getType() == String.class && StringUtils.isNotBlank((String) value))
					|| ClassUtils.isPrimitiveOrWrapper(field.getType())) {
				appender.append("example.").append(field.getName()).append("=").append(value).append("&");
			}
		}

		return appender;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pagination<E> search(@SuppressWarnings("rawtypes") SearcherProcessor processor) {
		return processor.process(this);
	}

	protected abstract void init();

	protected abstract void reset();
}
