package com.uhubai.server.config;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ServiceConfig{

    private String id;
    private String name;
    //    private String impl;
    private String ref;

}
