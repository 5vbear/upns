/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.myctu.platform.protocol;

import java.io.Serializable;
import java.util.Map;

/**
 * @since 2011-11-24
 * @author pippo
 */
public interface MapSerializable extends Serializable {

	Map<String, Object> toMap();

	void fromMap(Map<String, Object> in);

}
