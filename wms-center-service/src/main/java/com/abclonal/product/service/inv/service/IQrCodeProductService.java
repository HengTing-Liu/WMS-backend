package com.abclonal.product.service.inv.service;
import com.abclonal.product.api.domain.request.inv.CreateQrcodeRequest;
import com.abclonal.product.api.domain.response.inv.CreateQrcodeResponse;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;


public interface IQrCodeProductService {
    // 调用路径: /IQrCodeProductService/createQrcode
    @GetMapping("/createQrcode")
    CreateQrcodeResponse createQrcode(@SpringQueryMap CreateQrcodeRequest qrcodeRequest);
}