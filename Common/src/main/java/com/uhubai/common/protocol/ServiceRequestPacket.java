package com.uhubai.common.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ServiceRequestPacket extends Packet {

    private String id;

    private String className;

    private String methodName;

    private Class<?>[] parameterTypes;

    //private ServiceDescriptor serviceDescriptor;

    private Object[] args;

    private Class<?> returnType;

    private String serviceName;

    @Override
    public Byte getType() {
        return PacketType.SERVICE_REQUEST;
    }
}
