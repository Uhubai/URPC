package com.uhubai.client.proxy;

public interface ProxyFactory {
    Object getProxy(Class<?> clazz,String serviceName);
}
