package com.uhubai.client.proxy.impl;

import com.uhubai.client.proxy.InvokerHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

@Slf4j
public class InvokerInterceptor extends InvokerHandler implements MethodInterceptor {


//    private NettyClient client = null;
    //private String serviceName;

    public InvokerInterceptor(Class<?> clazz,String serviceName){
        super(clazz,serviceName);

    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return doInvoke(method, objects);
    }
}