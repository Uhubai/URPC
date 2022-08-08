package com.uhubai.client.loadbalance.impl;



import com.uhubai.client.loadbalance.LoadBalance;
import com.uhubai.client.register.Invoker;
import com.uhubai.common.protocol.ServiceRequestPacket;

import java.util.List;

public abstract class AbstractLoadBalance implements LoadBalance {
    //一部分公共逻辑
    public Invoker select(List<Invoker> invokers, ServiceRequestPacket requestPacket){
        if(invokers == null || invokers.isEmpty()){
            return null;
        }
        if(invokers.size() == 1){
            return invokers.get(0);
        }
        return doSelect(invokers, requestPacket);
    }

    public abstract String getName();

    protected abstract Invoker doSelect(List<Invoker> invokers, ServiceRequestPacket requestPacket);
}
