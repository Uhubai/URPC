package com.uhubai.client.config;

import com.uhubai.client.loadbalance.impl.ConsistentHashLoadBalance;
import com.uhubai.client.loadbalance.impl.LoadBalanceManager;
import com.uhubai.client.loadbalance.impl.RandomLoadBalance;
import com.uhubai.client.proxy.impl.CGLIBProxyFactory;
import com.uhubai.client.proxy.impl.JDKProxyFactory;
import com.uhubai.client.proxy.impl.ProxyManager;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "rpc.consumer.client")
public class ClientConfig implements InitializingBean {

    private String id;
    private String loadBalance = "hash";
    private String proxy = "jdk";

    @Override
    public void afterPropertiesSet() {
        ProxyManager proxyManager = ProxyManager.getInstance();
        if(proxy .equals("cglib")){
            proxyManager.setProxyClass(CGLIBProxyFactory.class);
        }else if(proxy.equals("jdk")){
            proxyManager.setProxyClass(JDKProxyFactory.class);
        }else{
            log.error("Proxy config err, no such proxy {}",proxy);
        }
        LoadBalanceManager loadBalanceManager = LoadBalanceManager.getInstance();
        if(loadBalance.equals("random")){
            loadBalanceManager.setLoadBalanceClass(RandomLoadBalance.class);
        }else if(loadBalance.equals("hash")){
            loadBalanceManager.setLoadBalanceClass(ConsistentHashLoadBalance.class);
        }else{
            log.error("LoadBalance config err, no such LoadBalance {}",loadBalance);
        }
    }
}