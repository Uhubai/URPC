package com.uhubai.server.service.impl;

import com.uhubai.server.anno.URpcService;
import com.uhubai.server.service.SayHelloService;
import lombok.extern.slf4j.Slf4j;

@URpcService(name = "SayHello")
@Slf4j
public class SayHelloImpl implements SayHelloService {
    @Override
    public void sayHello() {
        log.info("Hello!");
        log.info("Hello!");
    }

    public String OKOK(){
        return "OKOK";
    }
}
