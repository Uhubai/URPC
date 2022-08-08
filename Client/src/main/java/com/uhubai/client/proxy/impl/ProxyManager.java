package com.uhubai.client.proxy.impl;

import com.uhubai.client.proxy.ProxyFactory;
import com.uhubai.common.utils.ReflectionUtil;
import lombok.Data;

@Data
public class ProxyManager implements ProxyFactory {

    private Class<? extends ProxyFactory> DefaultProxyFactory = JDKProxyFactory.class;

    private ProxyFactory proxyFactory = null;

    private ProxyManager(){}

    private static ProxyManager INSTANCE;

    public static ProxyManager getInstance(){
        if(INSTANCE == null){
            INSTANCE = new ProxyManager();
        }
        return INSTANCE;
    }


    public void setProxyClass(Class<? extends ProxyFactory> proxyFactory){
        this.DefaultProxyFactory = proxyFactory;
    };


    @Override
    public Object getProxy(Class<?> clazz,String serviceName) {
        if(proxyFactory == null){
            proxyFactory = ReflectionUtil.newInstance(DefaultProxyFactory);
        }
        assert proxyFactory != null;
        return proxyFactory.getProxy(clazz,serviceName);
    }
}