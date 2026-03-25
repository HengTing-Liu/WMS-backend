package com.abclonal.product.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@Slf4j
public class ConfigController {
    @Value("${" + "custom.config:default value" + "}")// 从Nacos读取 custom.config 的值
    private String configValue;

    @Value("${" + "spring.application.name:unknown" + "}")
    private String appName;

    @Autowired
    private org.springframework.core.env.Environment env;

    @GetMapping("/config")
    public String getConfig() {
        log.info("appName={}",appName);
        log.info("custom={}",env.getProperty("custom.config"));
        return "从Nacos获取的配置: " + configValue;
    }
}
