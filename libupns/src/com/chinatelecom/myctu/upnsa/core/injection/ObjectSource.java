/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa.core.injection;

//TODO javadoc
abstract class ObjectSource<T> {
    DependencyInjectingObjectFactory dependencyInjectingObjectFactory;

    ObjectSource(DependencyInjectingObjectFactory aDependencyInjectingObjectFactory) {
        dependencyInjectingObjectFactory = aDependencyInjectingObjectFactory;
    }

    abstract T getObject();
}