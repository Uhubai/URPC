package com.uhubai.common.protocol.codec;

import com.uhubai.common.protocol.Packet;
import com.uhubai.common.protocol.PacketCodec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Encoder extends MessageToByteEncoder<Packet> {

    PacketCodec packetCodec = PacketCodec.getInstance();

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) throws Exception {
        packetCodec.encode(byteBuf,packet);
    }
}
