package com.abtk.product.web.controller.feign;


import com.abtk.product.api.domain.request.inv.CreateQrcodeRequest;
import com.abtk.product.api.domain.response.inv.CreateQrcodeResponse;
import com.abtk.product.service.inv.service.IQrCodeProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/QrCodeProduct")
@Slf4j
public class QrCodeProductController {

    @Autowired
    private IQrCodeProductService QrCodeProductService;


    @GetMapping("/createQrcode")
    CreateQrcodeResponse createQrcode(CreateQrcodeRequest qrcodeRequest) {

        return QrCodeProductService.createQrcode(qrcodeRequest);
    }



}
