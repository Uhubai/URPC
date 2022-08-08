package com.uhubai.client.proxy;

import com.uhubai.client.loadbalance.impl.LoadBalanceManager;
import com.uhubai.client.netty.ClientManager;
import com.uhubai.client.netty.NettyClient;
import com.uhubai.client.register.Invoker;
import com.uhubai.client.register.ServiceDiscovery;
import com.uhubai.common.protocol.ServiceRequestPacket;
import com.uhubai.common.protocol.ServiceResponsePacket;
import com.uhubai.common.utils.IDUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.List;

@Slf4j
public class InvokerHandler {
    private final Class<?> clazz;

    private final String serviceName;

    private final ServiceDiscovery serviceDiscovery = ServiceDiscovery.getInstance();

    public InvokerHandler(Class<?> clazz, String serviceName) {
        this.clazz = clazz;
        this.serviceName = serviceName;
    }

    public Object doInvoke(Method method, Object[] args) throws InterruptedException {
        ServiceRequestPacket requestPacket = new ServiceRequestPacket();

        requestPacket.setId(IDUtil.getUUID());

        requestPacket.setClassName(clazz.getName());

        requestPacket.setMethodName(method.getName());

        requestPacket.setArgs(args);

        requestPacket.setParameterTypes(method.getParameterTypes());

        requestPacket.setReturnType(method.getReturnType());

        requestPacket.setServiceName(serviceName);

//        requestPacket.setServiceDescriptor(ServiceDescriptor.from(clazz, method));
//
//        requestPacket.setArgs(objects);

        //通过serviceName获取server列表
        serviceDiscovery.discovery(serviceName);

        List<Invoker> invokers = serviceDiscovery.getServerList(serviceName);
        //负载均衡
        Invoker invoker = LoadBalanceManager.getInstance().select(invokers, requestPacket);
        if (invoker == null) {
            log.warn("service not fount : [{}]", requestPacket.getClassName() + "." + requestPacket.getMethodName());
            return null;
        }
        NettyClient client = ClientManager.getInstance().getClient(invoker.getAddress());

        ServiceResponsePacket responsePacket = (ServiceResponsePacket) client.send(requestPacket);

        if (responsePacket.getCode() == 0) {
            log.info("invoke success, remote:{} , load balance: {}", invoker.getAddress(), LoadBalanceManager.currLoadBalance);
            return responsePacket.getData();
        } else {
            log.error("invoke error ! code: " + responsePacket.getCode() + ". error info: " + responsePacket.getMessage());
            return null;
        }

    }
}
