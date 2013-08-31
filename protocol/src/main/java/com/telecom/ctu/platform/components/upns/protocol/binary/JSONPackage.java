/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.protocol.binary;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.myctu.platform.protocol.MapSerializable;
import com.telecom.ctu.platform.components.upns.protocol.BusinessPackageType;
import com.telecom.ctu.platform.components.upns.protocol.TransformPackage;

/**
 * @project protocol
 * @date 2013-8-18-下午12:56:50
 * @author pippo
 */
public class JSONPackage implements TransformPackage {

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

	public JSONPackage(MapSerializable payload) {
		this.type = BusinessPackageType.from(payload.getClass());
		this.payload = payload;
	}

	public JSONPackage(byte[] _package) throws Exception {
		type = BusinessPackageType.from(_package[0]);
		@SuppressWarnings("unchecked") Map<String, Object> in = objectMapper.readValue(Arrays.copyOfRange(_package,
			1,
			_package.length), Map.class);

		payload = type.clazz.newInstance();
		payload.fromMap(in);
	}

	public byte[] toByteArray() throws Exception {
		byte[] _payload = objectMapper.writeValueAsBytes(payload.toMap());
		ByteBuffer bb = ByteBuffer.allocate(1 + _payload.length);
		bb.put(type.code);
		bb.put(_payload);
		return bb.array();
	}

	public final BusinessPackageType type;

	public final MapSerializable payload;

	@Override
	public String toString() {
		return String.format("JSONPackage [type=%s, payload=%s]", type, payload);
	}

}
