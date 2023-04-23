package com.j3mall.openai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProxyApiController {

    @GetMapping("/proxy")
    public String proxy() {
        return "proxy";
    }

}
