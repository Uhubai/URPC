package com.uhubai.server.config;


import com.uhubai.server.netty.NettyServer;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "rpc.provider.server")
public class ServerConfig implements InitializingBean {

    private String id;

    private String ip;

    private int port;

    private int weight;

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
