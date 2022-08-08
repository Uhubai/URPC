package com.uhubai.client.loadbalance;


import com.uhubai.client.register.Invoker;
import com.uhubai.common.protocol.ServiceRequestPacket;

import java.util.List;

public interface LoadBalance {

    Invoker select(List<Invoker> invokers, ServiceRequestPacket requestPacket);
}
