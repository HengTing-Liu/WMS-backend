package com.abtk.product.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "wms-center", path = "/test1")
public interface Test1ServiceClient {
    // 调用路径: /test1/{id}
    @GetMapping("/{id}")
    String getUserById(@PathVariable("id") Long id);
}