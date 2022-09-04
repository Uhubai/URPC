package com.uhubai.client.config;

import com.uhubai.client.anno.URpcReference;
import com.uhubai.client.proxy.impl.ProxyManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;

@Slf4j
@Configuration
public class BeanPostProcess implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Field[] fields = bean.getClass().getFields();
        try {
            for(Field field:fields){
                if(field.isAnnotationPresent(URpcReference.class)){
                    String serviceName = field.getAnnotation(URpcReference.class).name();
                    log.info("代理生成中---");
                    field.set(bean,ProxyManager.getInstance().getProxy(field.getType(),serviceName));
                    log.info("代理生成完成---");
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        //return bean;

        //TODO 获取远程调用
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}