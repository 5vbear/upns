/*
 *  Copyright © 2010 www.myctu.cn. All rights reserved.
 */
package com.sirius.upns.server.node.service;

import com.sirius.upns.server.node.domain.model.Group;

import java.util.List;

/**
 * @project node-server
 * @date 2013-9-12-下午2:10:56
 * @author pippo
 */
public interface GroupExporterService {

    String create(Group group);

	void join(String groupId, List<String> userIds);

	void joinOnCreate(String groupId, int appId, String name, List<String> userIds);

	void quit(String groupId, List<String> userIds);

}
