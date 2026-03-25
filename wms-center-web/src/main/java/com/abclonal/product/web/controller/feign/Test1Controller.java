package com.abclonal.product.web.controller.feign;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test1")
@Slf4j
public class Test1Controller {
    @GetMapping("/{id}")
    public String getUserById(@PathVariable Long id) {
        log.info("id={}通过test1获取信息",id);
        return "id="+id+"通过test1获取信息";
    }
}
