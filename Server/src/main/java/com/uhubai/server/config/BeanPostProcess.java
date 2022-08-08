package com.uhubai.server.config;

import com.uhubai.server.anno.URpcService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanPostProcess implements BeanPostProcessor {

    @Autowired
    ServiceManager manager;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean.getClass().isAnnotationPresent(URpcService.class)){
            manager.register(bean.getClass().getAnnotation(URpcService.class).name(),bean);
        }
        return bean;
    }
}
