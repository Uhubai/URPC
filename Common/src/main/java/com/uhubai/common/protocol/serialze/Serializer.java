package com.uhubai.common.protocol.serialze;

import com.uhubai.common.protocol.serialze.impl.JSONSerializerImpl;

public interface Serializer {
    public static Serializer INSTANCE = new JSONSerializerImpl();

    Byte getSerializeType();

    byte[] serialize(Object object);

    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
