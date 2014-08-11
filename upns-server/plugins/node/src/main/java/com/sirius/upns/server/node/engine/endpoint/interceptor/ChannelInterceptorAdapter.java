/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.engine.endpoint.interceptor;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler.Sharable;

/**
 * @project node-server
 * @date 2013-8-18-上午9:35:50
 * @author pippo
 */
@Sharable
public abstract class ChannelInterceptorAdapter extends ChannelDuplexHandler implements ChannelInterceptor {

}
