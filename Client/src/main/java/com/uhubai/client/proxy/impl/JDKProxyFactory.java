package com.uhubai.client.proxy.impl;

import com.uhubai.client.proxy.ProxyFactory;

import java.lang.reflect.Proxy;

public class JDKProxyFactory implements ProxyFactory {
    @Override
    public Object getProxy(Class<?> clazz,String serviceName) {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{clazz}, new InvokerInvocationHandler(clazz,serviceName));
    }
}