package com.uhubai.client.proxy.impl;

import com.uhubai.client.proxy.InvokerHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class InvokerInvocationHandler extends InvokerHandler implements InvocationHandler {

    //String serviceName;

    public InvokerInvocationHandler(Class<?> clazz,String serviceName) {
        super(clazz,serviceName);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("执行JDK代理--");
        return doInvoke(method, args);
    }
}
