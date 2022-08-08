package com.uhubai.server.netty;

import com.uhubai.common.protocol.ServiceRequestPacket;
import com.uhubai.server.dispatcher.ChannelEventRunnable;
import com.uhubai.server.dispatcher.Dispatcher;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@ChannelHandler.Sharable
public class ServiceRequestHandler extends SimpleChannelInboundHandler<ServiceRequestPacket> {

    public static final ServiceRequestHandler INSTANCE = new ServiceRequestHandler();

    private Dispatcher dispatcher = Dispatcher.getInstance();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端建立了连接");
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ServiceRequestPacket serviceRequest) throws Exception {
        /**
         * 将事件分发到线程池上执行
         */
        dispatcher.execute(new ChannelEventRunnable(channelHandlerContext.channel(), serviceRequest));

    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端断开了连接");
        super.channelInactive(ctx);
    }
}
