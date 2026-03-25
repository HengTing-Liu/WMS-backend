package com.abclonal.product.web;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan({"com.abclonal.product.dao.**.mapper", "com.abclonal.product.dao.custom", "com.abclonal.product.dao.generator"})
@ComponentScan({"com.abclonal.product"})
@EnableDiscoveryClient  // 启用服务发现
@EnableFeignClients(basePackages = "com.abclonal.product.service.feign")
@Slf4j// 启用 Feign 客户端
public class Application {
    public static void main(String[] args) {
        String profile = System.getProperty("spring.profiles.active");
        log.info("=== Maven Profile: " + System.getProperty("build.env") + " ===");
        log.info("=== Spring Profile: " + profile + " ===");
        SpringApplication.run(Application.class, args);
        log.info("server start is success");
    }
}