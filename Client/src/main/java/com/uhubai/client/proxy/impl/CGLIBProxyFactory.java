package com.uhubai.client.proxy.impl;

import com.uhubai.client.proxy.ProxyFactory;
import org.springframework.cglib.proxy.Enhancer;

public class CGLIBProxyFactory implements ProxyFactory {
    @Override
    public Object getProxy(Class<?> clazz,String serviceName) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new InvokerInterceptor(clazz,serviceName));
        return enhancer.create();
    }
}