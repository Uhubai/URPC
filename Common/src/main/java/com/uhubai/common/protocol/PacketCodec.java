package com.uhubai.common.protocol;

import com.uhubai.common.protocol.serialze.Serializer;
import com.uhubai.common.protocol.serialze.SerializerType;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;


public class PacketCodec {
    public static final int MAGIC_NUMBER = 0x12345678;

    private Map<Byte, Class<? extends Packet>> packetTypeMap;

    private Map<Byte, Serializer> serializerTypeMap;

    private static PacketCodec INSTANCE = new PacketCodec();

    public  static PacketCodec getInstance(){
        return PacketCodec.INSTANCE;
    }

    Serializer jsonSerialize;

    public PacketCodec() {

        packetTypeMap = new HashMap<>();
        serializerTypeMap = new HashMap<>();

        packetTypeMap.put(PacketType.HEARTBEAT, HeartbeatPacket.class);
        packetTypeMap.put(PacketType.SERVICE_REQUEST, ServiceRequestPacket.class);
        packetTypeMap.put(PacketType.SERVICE_RESPONSE, ServiceResponsePacket.class);
        jsonSerialize = Serializer.INSTANCE;

        serializerTypeMap.put(SerializerType.JSON, jsonSerialize);

    }

    public void encode(ByteBuf byteBuf, Packet packet) {

        //序列化对象
        byte[] bytes = jsonSerialize.serialize(packet);

        //进行编码

        byteBuf.writeInt(MAGIC_NUMBER);

        byteBuf.writeByte(packet.getVersion());

        byteBuf.writeByte(packet.getType());

        byteBuf.writeByte(jsonSerialize.getSerializeType());

        byteBuf.writeInt(bytes.length);

        byteBuf.writeBytes(bytes);

    }

    public Packet decode(ByteBuf byteBuf) {
        //校验魔数
        byteBuf.skipBytes(4);
        //校验版本号
        byteBuf.skipBytes(1);
        //获取包类型
        byte packetType = byteBuf.readByte();
        //获取序列化方法
        byte serializationType = byteBuf.readByte();
        //获取数据包长度
        int length = byteBuf.readInt();
        //获取数据包
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        //反序列化
        Class<? extends Packet> packetClass = packetTypeMap.get(packetType);
        Serializer serializer = serializerTypeMap.get(serializationType);
        if (packetClass != null && serializer != null) {
            return serializer.deserialize(packetClass, bytes);
        }

        return null;

    }
}
