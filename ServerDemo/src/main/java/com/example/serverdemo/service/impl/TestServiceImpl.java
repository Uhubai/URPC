package com.example.serverdemo.service.impl;

import com.example.serverdemo.service.TestService;
import com.uhubai.server.anno.URpcService;

@URpcService(name = "TestService")
public class TestServiceImpl implements TestService {
    @Override
    public String test() {
        return "Service Test";
    }
}
