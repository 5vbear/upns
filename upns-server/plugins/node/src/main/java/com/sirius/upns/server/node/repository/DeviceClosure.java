package com.sirius.upns.server.node.repository;

import com.sirius.upns.server.node.domain.model.Device;

/**
 * Created by pippo on 14-5-29.
 */
public interface DeviceClosure {

	void execute(Device device);

}
