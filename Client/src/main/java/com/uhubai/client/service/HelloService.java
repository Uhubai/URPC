package com.uhubai.client.service;

import com.uhubai.client.anno.URpcReference;
import com.uhubai.client.reference.SayHello;
import org.springframework.stereotype.Component;

@Component
public class HelloService {
    @URpcReference( name = "SayHello")
    public SayHello sayHello;


    public SayHello getSayHello() {
        return sayHello;
    }
}
