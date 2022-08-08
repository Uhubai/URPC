package com.uhubai.server;


import com.uhubai.common.protocol.ServiceRequestPacket;
import com.uhubai.server.config.ServiceManager;
import com.uhubai.server.netty.ServiceInvoker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Method;

@SpringBootTest
class ServerApplicationTests {

    @Autowired
    ServiceManager manager;
    @Test
    void contextLoads() throws NoSuchMethodException {
        Object object = manager.getMap().get("SayHello");
        Method method = object.getClass().getMethod("sayHello", null);
        ServiceInvoker serviceInvoker = ServiceInvoker.INSTANCE;
        ServiceRequestPacket requestPacket = new ServiceRequestPacket();
        requestPacket.setReturnType(void.class);
        Object invoke = serviceInvoker.invoke(object, method,requestPacket);
    }

}
