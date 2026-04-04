package com.abtk.product.service.inv.impl;
import com.abtk.product.api.domain.request.inv.CreateQrcodeRequest;
import com.abtk.product.api.domain.response.inv.CreateQrcodeResponse;
import com.abtk.product.dao.entity.QrCodeEntity;
import com.abtk.product.dao.mapper.QrCodeEntityMapper;
import com.abtk.product.domain.converter.QrCodeConverter;
import com.abtk.product.service.inv.service.IQrCodeProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
@Slf4j
public class QrcodeProductServiceimpl implements IQrCodeProductService {
    /**
     * 获取最近的异常信息（需要结合异常上下文）
     */
    @Autowired
    private QrCodeEntityMapper qrcodeMapper;

    @Override
    public CreateQrcodeResponse createQrcode(CreateQrcodeRequest request) {
        CreateQrcodeResponse response = new CreateQrcodeResponse();
        int quantity = request.getQty();

        List<QrCodeEntity> entities = new ArrayList<>();
        int maxRetries = 3;
        int retryCount = 0;

        while (entities.size() < quantity && retryCount < maxRetries) {
            // 一次生成剩余需要的数量
            int needCount = quantity - entities.size();
            List<String> newCodes = generateBatchQrcodes(needCount);

            // 转换为实体
            List<QrCodeEntity> newEntities = newCodes.stream()
                    .map(code -> {
                        QrCodeEntity entity = QrCodeConverter.INSTANCE.requestToQrCode(request);
                        entity.setQrcode(code);
                        entity.setCreateTime(null);
                        return entity;
                    })
                    .collect(Collectors.toList());

            try {
                // 尝试批量插入（利用数据库唯一索引过滤重复）
                int inserted = qrcodeMapper.batchInsert(newEntities);
                if (inserted > 0) {
                    entities.addAll(newEntities.subList(0, inserted));
                }
            } catch (DuplicateKeyException e) {
                // 有重复，继续重试
                retryCount++;
            }
        }

        // 返回生成的码
        List<String> qrcodeList = entities.stream()
                .map(QrCodeEntity::getQrcode)
                .collect(Collectors.toList());
        response.setQrcodelist(qrcodeList);

        return response;
    }

    /**
     * 批量保存到数据库（一次性插入）
     */
    private void tryBatchInsert(List<QrCodeEntity> qrcodeList) {
        // 方法2：使用 MyBatis XML
        qrcodeMapper.batchInsert(qrcodeList);
    }

    /**
     * 批量生成指定数量的二维码
     */
    private List<String> generateBatchQrcodes(int count) {
        List<String> batch = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            batch.add(generateSingleQrcode());
        }
        return batch;
    }

    /**
     * 生成单个二维码
     */
    private String generateSingleQrcode() {
        String letters = generateRandomLetters(3);
        String numbers = generateRandomNumbers(7);
        return letters + numbers;
    }

    /**
     * 生成唯一的二维码码值
     * 规则：前3位随机字母 + 后7位随机数字
     */
    private String generateUniqueQrcode() {
        String qrcode;

            // 生成前3位随机字母
            String letters = generateRandomLetters(3);

            // 生成后7位随机数字
            String numbers = generateRandomNumbers(7);

            // 组合成完整的二维码码值
            qrcode = letters + numbers;
        return qrcode;
    }

    /**
     * 生成指定长度的随机字母
     */
    private String generateRandomLetters(int length) {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder result = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(letters.length());
            result.append(letters.charAt(index));
        }

        return result.toString();
    }

    /**
     * 生成指定长度的随机数字
     */
    private String generateRandomNumbers(int length) {
        StringBuilder result = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10); // 生成0-9的随机数
            result.append(digit);
        }

        return result.toString();
    }

}
