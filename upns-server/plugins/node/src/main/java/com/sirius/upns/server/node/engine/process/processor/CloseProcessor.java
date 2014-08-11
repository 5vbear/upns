/*
 *  Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.engine.process.processor;

import com.sirius.upns.protocol.business.sub.Close;
import com.sirius.upns.server.node.engine.process.AbstractProcessor;
import com.sirius.upns.server.node.engine.process.PackageProcessException;
import com.sirius.upns.server.node.engine.process.ProcessContext;
import com.sirius.upns.server.node.engine.process.ProcessEngine;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


/**
 * @author pippo
 * @project node-server
 * @date 2013-8-26-下午4:09:20
 */
@Component
public class CloseProcessor extends AbstractProcessor<Close> {

    @Resource
    private ProcessEngine engine;

    @Override
    public void process(ProcessContext context) throws PackageProcessException {
        context.close();
    }

    @PostConstruct
    public void init() {
        engine.regist(this);
    }
}
