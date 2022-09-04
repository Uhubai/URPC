package com.example.clienttest;


import com.example.clienttest.controller.TestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ClientTestApplicationTests {

    @Autowired
    TestController testController;

    @Test
    void contextLoads() {
        System.out.println(testController.test.test());
    }

}
