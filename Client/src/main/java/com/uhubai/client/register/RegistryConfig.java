package com.uhubai.client.register;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "rpc.registry")
public class RegistryConfig {
    private String id;
    private String registryAddress = "127.0.0.1:2181";
    private String zkPath;
    private int sessionTimeout=20000;
    private int connectionTimeout=20000;

}
