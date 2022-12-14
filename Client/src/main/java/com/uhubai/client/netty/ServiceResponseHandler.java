package com.uhubai.client.netty;


import com.uhubai.common.protocol.ServiceRequestPacket;
import com.uhubai.common.protocol.ServiceResponsePacket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;

@Slf4j
@NoArgsConstructor
public class ServiceResponseHandler extends SimpleChannelInboundHandler<ServiceResponsePacket> {


    private final Map<String, SynchronousQueue<Object>> queueMap = new ConcurrentHashMap<>();
    private NettyClient client = null;

    public ServiceResponseHandler(NettyClient client) {
        this.client = client;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Client connected.");
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ServiceResponsePacket serviceResponsePacket) throws Exception {
        String id = serviceResponsePacket.getRequestId();
        SynchronousQueue<Object> queue = queueMap.get(id);

        if(queue != null){
            queue.put(serviceResponsePacket);
            queueMap.remove(id);
        }else{
            log.error("request id error !!!");
        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("Client disconnected.");
        //销毁所有queueMap中等待响应的包
        for(String id : queueMap.keySet()){
            ServiceResponsePacket responsePacket = new ServiceResponsePacket();
            responsePacket.setCode(-1);
            responsePacket.setMessage("Client disconnected.");
            responsePacket.setRequestId(id);
            SynchronousQueue<Object> queue = queueMap.get(id);
            queue.put(responsePacket);
        }
        super.channelInactive(ctx);
        client.close();
    }

    public SynchronousQueue<Object> sendRequest(ServiceRequestPacket requestPacket, Channel channel){
        SynchronousQueue<Object> queue = new SynchronousQueue<>();
        queueMap.put(requestPacket.getId(), queue);
        channel.writeAndFlush(requestPacket);
        return queue;
    }
}
