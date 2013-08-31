/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.protocol;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @project protocol
 * @date 2013-8-31-上午9:11:54
 * @author pippo
 */
public class MD5Utils {

	static MessageDigest getDigest() {
		try {
			return MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

	}

	public static byte[] md5(byte data[]) {
		return getDigest().digest(data);
	}

	public static byte[] md5(String data) {
		return md5(data.getBytes());
	}

	public static String md5Hex(byte data[]) {
		return toHexString(md5(data));
	}

	public static String md5Hex(String data) {
		return toHexString(md5(data));
	}

	public static String toHexString(byte b[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_CHARS.charAt(b[i] >>> 4 & 0xf));
			sb.append(HEX_CHARS.charAt(b[i] & 0xf));
		}

		return sb.toString();
	}

	public static byte[] toByteArray(String s) {
		byte buf[] = new byte[s.length() / 2];
		int j = 0;
		for (int i = 0; i < buf.length; i++)
			buf[i] = (byte) (Character.digit(s.charAt(j++), 16) << 4 | Character.digit(s.charAt(j++), 16));

		return buf;
	}

	private static final String HEX_CHARS = "0123456789abcdef";

}
