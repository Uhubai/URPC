package com.uhubai.server.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "rpc.registry")
public class RegistryConfig {
    private String id;
    private String registryAddress ;
    private String zkPath;
    private int sessionTimeout;
    private int connectionTimeout;

}
