package com.abtk.product.service.feign.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class QrCodeProductServiceFallback {
    // Feign 调用失败时的降级处理（兜底返回空结果）
    // 若后续需要恢复 QrCodeProductServiceClient，请补充对应降级逻辑
}
