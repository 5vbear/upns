/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.protocol.transfer;

import com.sirius.upns.protocol.business.BusinessPacket;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import java.io.IOException;
import java.util.Map;


/**
 * @project protocol
 * @date 2013-9-9-下午2:47:36
 * @author pippo
 */
public class JacksonMarshaller implements PayloadMarshaller {

	public final static ObjectMapper objectMapper = new ObjectMapper();

	public final static JsonFactory jsonFactory = new JsonFactory();

	static {
		objectMapper.configure(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS, true);
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		objectMapper.setSerializationInclusion(Inclusion.NON_EMPTY);
		objectMapper.configure(SerializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS, true);
		objectMapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
		objectMapper.configure(SerializationConfig.Feature.WRITE_EMPTY_JSON_ARRAYS, false);

		objectMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		objectMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
	}

	@Override
	public byte[] marshal(BusinessPacket payload) throws IOException {
		return objectMapper.writeValueAsBytes(payload.toMap());
	}

	@Override
	public <T extends BusinessPacket> T unmarshal(byte[] content, Class<T> payloadClass) throws Exception {
		@SuppressWarnings("unchecked") Map<String, Object> in = objectMapper.readValue(content, Map.class);

		T payload = payloadClass.newInstance();
		payload.fromMap(in);
		return payload;
	}
}