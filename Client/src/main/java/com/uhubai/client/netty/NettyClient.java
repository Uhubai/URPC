package com.uhubai.client.netty;

import com.alibaba.fastjson.JSON;
import com.uhubai.client.register.ServiceDiscovery;
import com.uhubai.common.protocol.ServiceRequestPacket;
import com.uhubai.common.protocol.ServiceResponsePacket;
import com.uhubai.common.protocol.codec.Decoder;
import com.uhubai.common.protocol.codec.Encoder;
import com.uhubai.common.protocol.codec.Spliter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.Date;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class NettyClient {

    private Bootstrap bootstrap;

    private String host;

    private int port;

    private ServiceDiscovery serviceDiscovery = ServiceDiscovery.getInstance();

    private ServiceResponseHandler serviceResponseHandler = new ServiceResponseHandler(this);

    private Channel channel;

    private NioEventLoopGroup workerGroup = null;

    private ClientManager clientManager = ClientManager.getInstance();

    public NettyClient(String host, int port) {

        this.host = host;
        this.port = port;

        bootstrap = new Bootstrap();

        workerGroup = new NioEventLoopGroup();

        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new Spliter());
                        socketChannel.pipeline().addLast(new Decoder());
                        socketChannel.pipeline().addLast(new IdleStateHandler(0,0,15, TimeUnit.SECONDS));
                        socketChannel.pipeline().addLast(new HeartbeatHandler());
                        socketChannel.pipeline().addLast(serviceResponseHandler);
                        socketChannel.pipeline().addLast(new Encoder());
                    }
                });
        try {
            this.connect(host, port);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }

    public void connect(String host, int port) throws InterruptedException {
        ChannelFuture channelFuture = bootstrap.connect(host, port).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println(new Date() + ": connect to " + host + ":" + port);
                } else {
                    System.out.println(new Date() + ": failed...");
                }
            }
        });
        this.channel = channelFuture.sync().channel();
    }

    public Object send(ServiceRequestPacket requestPacket) throws InterruptedException {
        if(channel != null && channel.isActive()){
            SynchronousQueue<Object> queue = serviceResponseHandler.sendRequest(requestPacket, channel);
            ServiceResponsePacket result = (ServiceResponsePacket)queue.take();
            Class<?> returnType = requestPacket.getReturnType();
            Object newData = parseReturnType(returnType, result.getData());
            result.setData(newData);
            return result;
        }else {
            ServiceResponsePacket responsePacket = new ServiceResponsePacket();
            responsePacket.setCode(1);
            responsePacket.setMessage("???????????????????????????.???????????????????????????!");
            return responsePacket;
        }

    }
    public void close(){
        clientManager.removeClient(host + ":" + port);
        workerGroup.shutdownGracefully();
    }

    /**
     * ????????????
     * @param returnType
     * @param data
     * @return
     */
    private Object parseReturnType(Class<?> returnType, Object data){
        return JSON.parseObject(JSON.toJSONString(data), returnType);
    }


}
