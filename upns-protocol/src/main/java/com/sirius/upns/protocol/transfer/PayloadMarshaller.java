/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.protocol.transfer;

import com.sirius.upns.protocol.business.BusinessPacket;


/**
 * @project protocol
 * @date 2013-9-9-下午2:39:24
 * @author pippo
 */
public interface PayloadMarshaller {

	/**
	 * 反序列化
	 * 
	 * @param content byte数组表示的内容
	 * @param payloadClass payload类型
	 * @return Packet
	 * @throws Exception
	 */
	<T extends BusinessPacket> T unmarshal(byte[] content, Class<T> payloadClass) throws Exception;

	/**
	 * 序列化
	 * 
	 * @param payload payload
	 * @return byte
	 */
	byte[] marshal(BusinessPacket payload) throws Exception;

}
