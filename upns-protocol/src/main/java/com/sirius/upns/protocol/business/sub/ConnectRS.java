/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.protocol.business.sub;

/**
 * @project protocol
 * @date 2013-8-18-下午3:22:20
 * @author pippo
 */
public class ConnectRS extends RS {

	private static final long serialVersionUID = 1754697275335392015L;

	//	public static final String HISTORIES = "h";

	public ConnectRS() {
		super();
	}

	public ConnectRS(boolean success) {
		super(success);
	}

	@Override
	public String toString() {
		return String.format("ConnectRS [success=%s, error_code=%s]", success, error_code);
	}

	//	@Override
	//	public Map<String, Object> toMap() {
	//		Map<String, Object> out = super.toMap();
	//		if (histories != null && !histories.isEmpty()) {
	//			out.put(HISTORIES, histories);
	//		}
	//		return out;
	//	}
	//
	//	@SuppressWarnings("unchecked")
	//	@Override
	//	public void fromMap(Map<String, Object> in) {
	//		super.fromMap(in);
	//		histories = (List<History>) in.get(HISTORIES);
	//	}
	//
	//	public List<History> histories;
	//
	//	@Override
	//	public String toString() {
	//		return String.format("ConnectRS [success=%s, error_code=%s, histories=%s]",
	//			success,
	//			error_code,
	//			histories != null ? Arrays.toString(histories.toArray()) : null);
	//	}

}
