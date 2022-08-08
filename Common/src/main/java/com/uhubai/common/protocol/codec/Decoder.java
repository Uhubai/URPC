package com.uhubai.common.protocol.codec;

import com.uhubai.common.protocol.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class Decoder extends ByteToMessageDecoder {

    PacketCodec packetCodec = PacketCodec.getInstance();

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        list.add(packetCodec.decode(byteBuf));
    }
}

