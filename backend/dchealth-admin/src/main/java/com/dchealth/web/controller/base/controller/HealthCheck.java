package com.dchealth.web.controller.base.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {

    @GetMapping("/isOk")
    public String healthCheck(){
        return "ok";
    }
}
