package com.uhubai.client.loadbalance.impl;


import com.uhubai.client.loadbalance.LoadBalance;
import com.uhubai.client.register.Invoker;
import com.uhubai.common.protocol.ServiceRequestPacket;
import com.uhubai.common.utils.ReflectionUtil;

import java.util.List;

public class LoadBalanceManager implements LoadBalance {
    public static String currLoadBalance = "random";
    private Class<? extends AbstractLoadBalance> loadBalanceClass = RandomLoadBalance.class;

    private static LoadBalanceManager INSTANCE = null;
    private LoadBalanceManager(){}
    public static LoadBalanceManager getInstance(){
        if(INSTANCE == null){
            INSTANCE = new LoadBalanceManager();
        }
        return INSTANCE;
    }

    private AbstractLoadBalance loadBalance = null;

    @Override
    public Invoker select(List<Invoker> invokers, ServiceRequestPacket requestPacket) {
        if(loadBalance == null){
            loadBalance = ReflectionUtil.newInstance(loadBalanceClass);
            currLoadBalance = loadBalance.getName();
        }
        return loadBalance.select(invokers, requestPacket);
    }


    public void setLoadBalanceClass(Class<? extends AbstractLoadBalance> loadBalanceClass) {
        this.loadBalanceClass = loadBalanceClass;
    }
}

