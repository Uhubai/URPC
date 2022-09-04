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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Data
@Configuration
public class ClientConfig implements InitializingBean {

    @Value("${rpc.consumer.client.id}")
    private String id;
    @Value("${rpc.consumer.client.loadBalance}")
    // = "hash"
    private String loadBalance;
    @Value("${rpc.consumer.client.proxy}")
    //="jdk"
    private String proxy;

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