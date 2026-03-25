//package com.abclonal.product.web;
//
//import feign.FeignException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@ControllerAdvice
//@Slf4j
//public class GlobalExceptionHandler {
//    /**
//     * 处理 Feign 调用异常
//     */
//    @ExceptionHandler(FeignException.class)
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> handleFeignException(FeignException e) {
//        log.error("Feign 调用异常: {}", e.getMessage());
//
//        Map<String, Object> error = new HashMap<>();
//        error.put("success", false);
//        error.put("message", "服务调用失败: " + e.getMessage());
//        error.put("timestamp", System.currentTimeMillis());
//
//        return ResponseEntity.status(500).body(error);
//    }
//
//    /**
//     * 处理其他异常
//     */
//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
//        log.error("系统异常: {}", e.getMessage(), e);
//
//        Map<String, Object> error = new HashMap<>();
//        error.put("success", false);
//        error.put("message", "系统异常: " + e.getMessage());
//        error.put("timestamp", System.currentTimeMillis());
//
//        return ResponseEntity.status(500).body(error);
//    }
//}
