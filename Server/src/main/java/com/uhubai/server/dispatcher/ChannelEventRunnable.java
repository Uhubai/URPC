package com.uhubai.server.dispatcher;


import com.uhubai.common.protocol.ServiceRequestPacket;
import com.uhubai.common.protocol.ServiceResponsePacket;
import com.uhubai.server.config.ServiceManager;
import com.uhubai.server.config.SpringUtil;
import com.uhubai.server.netty.ServiceInvoker;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;

@Slf4j
@Data
public class ChannelEventRunnable implements Runnable{
    private Channel channel;
    private ServiceRequestPacket serviceRequest;
    private String threadName;
    public ChannelEventRunnable(Channel channel, ServiceRequestPacket packet){
        this.channel = channel;
        this.serviceRequest = packet;
    }

    @Override
    public void run() {
        try {
            ServiceResponsePacket responsePacket = new ServiceResponsePacket();
            String currThread = Thread.currentThread().getName().split("-")[0];
            if(currThread == null || !currThread.equals(threadName)){
                responsePacket.setRequestId(serviceRequest.getId());
                responsePacket.setCode(5);
                responsePacket.setMessage("Server is busy");
            }else {
                //设置相应id
                responsePacket.setRequestId(serviceRequest.getId());

                ApplicationContext context = SpringUtil.getApplicationContext();

                log.info("serviceRequest:{}",serviceRequest);

                ServiceManager manager = context.getBean(ServiceManager.class);

                if (!manager.getMap().containsKey(serviceRequest.getServiceName())) {
                    log.info("No such service : " + serviceRequest);
                    responsePacket.setCode(1);
                    responsePacket.setMessage("No such service");
                } else {
                    //获取服务的实现类
                    // TODO NullPointException
                    Object object = manager.getMap().get(serviceRequest.getServiceName());
                    Method method = object.getClass().getMethod(serviceRequest.getMethodName(), serviceRequest.getParameterTypes());
                    ServiceInvoker serviceInvoker = ServiceInvoker.INSTANCE;
                    Object result = serviceInvoker.invoke(object, method, serviceRequest);
                    log.info("service id : " + serviceRequest.getId());
                    log.info("Service invoked : " + serviceRequest);
                    responsePacket.setCode(0);
                    responsePacket.setData(result);
                }
            }

            channel.writeAndFlush(responsePacket);
        } catch (NoSuchMethodException e) {
            log.error("dispatcher thread error," + e.getMessage());
            e.printStackTrace();
        }
    }
}
