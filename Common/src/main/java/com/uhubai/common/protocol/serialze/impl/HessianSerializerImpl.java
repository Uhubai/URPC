package com.uhubai.common.protocol.serialze.impl;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.uhubai.common.protocol.serialze.Serializer;
import com.uhubai.common.protocol.serialze.SerializerType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class HessianSerializerImpl implements Serializer {
    @Override
    public Byte getSerializeType() {
        return SerializerType.Hessian;
    }

    @Override
    public  byte[] serialize(Object javaBean) throws Exception {
        Hessian2Output ho = null;
        ByteArrayOutputStream baos = null;

        try {
            baos = new ByteArrayOutputStream();
            ho = new Hessian2Output(baos);
            ho.writeObject(javaBean);
            ho.flush();
            return baos.toByteArray();
        } catch (Exception ex) {
            System.out.println("[模拟日志记录]HessianUtils.serialize.异常." + ex.getMessage());
            throw new Exception("HessianUtils.serialize.异常.", ex);
        } finally {
            if (null != ho) {
                ho.close();
            }
        }
    }

    /**
     * JavaBean反序列化.
     * @param serializeData 序列化数据.
     */
    @Override
    public <T> T deserialize(Class<T> clazz,byte[] serializeData) throws Exception {
        T javaBean = null;
        Hessian2Input hi = null;
        ByteArrayInputStream bais = null;

        try {
            bais = new ByteArrayInputStream(serializeData);
            hi = new Hessian2Input(bais);
            javaBean = (T) hi.readObject();
            return javaBean;
        } catch (Exception ex) {
            System.out.println("[模拟日志记录]HessianUtils.deserialize.异常." + ex.getMessage());
            throw new Exception("HessianUtils.deserialize.异常.", ex);
        } finally {
            if (null != hi) {
                hi.close();
            }
        }
    }
}
