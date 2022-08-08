package com.uhubai.common.protocol;

public class HeartbeatPacket extends Packet {
    @Override
    public Byte getType() {
        return PacketType.HEARTBEAT;
    }
}
