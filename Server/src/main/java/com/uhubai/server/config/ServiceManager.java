package com.uhubai.server.config;

import com.uhubai.server.register.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class ServiceManager{
    Map<String,Object> map;

    @Autowired
    ServerConfig serverConfig;

    @Autowired
    RegistryConfig registryConfig;

    ServiceRegistry registry = ServiceRegistry.getInstance();


    ServiceManager(){
        map = new HashMap<>();
    }

    public void register(String name,Object object){
        if (serverConfig==null) {
            log.info("没有配置server，不能发布到注册中心");
            return;
        }
        if (registryConfig==null) {
            log.info("registry，不能发布到注册中心");
            return;
        }
        //连接zookeeper
        registry.connectServer(serverConfig,registryConfig);
        //将服务发布到注册中心
        registry.register(name);
        map.put(name,object);
    }

    public Map<String, Object> getMap() {
        return map;
    }
}
