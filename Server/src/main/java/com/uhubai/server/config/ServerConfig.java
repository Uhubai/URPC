package com.uhubai.server.config;


import com.uhubai.server.netty.NettyServer;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "rpc.provider.server")
public class ServerConfig implements InitializingBean {

    private String id;

    private String ip = "127.0.0.1";

    private int port = 7800;

    private int weight = 100;

    @Override
    public void afterPropertiesSet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NettyServer nettyServer = new NettyServer(port, weight);
                nettyServer.start();
            }
        }).start();
    }
}
