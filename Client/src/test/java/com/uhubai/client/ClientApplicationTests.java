package com.uhubai.client;

import com.uhubai.client.service.HelloService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ClientApplicationTests {

    @Autowired
    HelloService service;

    @Test
    void contextLoads() {
        //helloService.getSayHello().sayHello();
        System.out.println(service.getSayHello().OKOK());
    }

}
