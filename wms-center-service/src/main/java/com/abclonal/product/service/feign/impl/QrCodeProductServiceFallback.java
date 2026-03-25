package com.abclonal.product.service.feign.impl;

import com.abclonal.product.api.domain.request.inv.CreateQrcodeRequest;
import com.abclonal.product.api.domain.response.inv.CreateQrcodeResponse;
import com.abclonal.product.service.feign.QrCodeProductServiceClient;
import com.abclonal.product.service.feign.Test1ServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Slf4j
public class QrCodeProductServiceFallback implements QrCodeProductServiceClient {
    @Override
    public  CreateQrcodeResponse createQrcode(@SpringQueryMap CreateQrcodeRequest qrcodeRequest) {
        CreateQrcodeResponse response = new CreateQrcodeResponse();
        response.setQrcodelist(new ArrayList<>());  // 返回空列表
        return response;
    }

    /**
     * 获取最近的异常信息（需要结合异常上下文）
     */
    private String getLastException() {
        // 这里可以结合 Hystrix 或 Sentinel 的异常信息
        // 或者通过 ThreadLocal 存储最近的异常
        return "服务调用超时或不可用";
    }

}
