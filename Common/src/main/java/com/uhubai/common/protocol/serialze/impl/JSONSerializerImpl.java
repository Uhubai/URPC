package com.uhubai.common.protocol.serialze.impl;

import com.alibaba.fastjson.JSON;
import com.uhubai.common.protocol.serialze.Serializer;
import com.uhubai.common.protocol.serialze.SerializerType;


public class JSONSerializerImpl implements Serializer {
    @Override
    public Byte getSerializeType() {
        return SerializerType.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
