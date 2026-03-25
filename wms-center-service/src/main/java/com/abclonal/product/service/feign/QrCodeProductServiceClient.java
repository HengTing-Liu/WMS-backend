package com.abclonal.product.service.feign;
import com.abclonal.product.api.domain.request.inv.CreateQrcodeRequest;
import com.abclonal.product.api.domain.response.inv.CreateQrcodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "wms-center-web", path = "/QrCodeProduct")
public interface QrCodeProductServiceClient {
    // 调用路径: /QrCodeProductService/createQrcode
    @GetMapping("/createQrcode")
    CreateQrcodeResponse createQrcode(@SpringQueryMap CreateQrcodeRequest qrcodeRequest);
}