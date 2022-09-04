package com.example.clienttest.controller;

import com.example.clienttest.service.ClientTest;
import com.uhubai.client.anno.URpcReference;
import org.springframework.stereotype.Component;

@Component
public class TestController {
    @URpcReference(name = "TestService")
    public ClientTest test;
}
