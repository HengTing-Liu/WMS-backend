package com.abtk.product.domain.converter;

import com.abtk.product.api.domain.request.inv.CreateQrcodeRequest;
import com.abtk.product.dao.entity.QrCodeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 *
 * @description:
 * @author: 75618
 * @time: 2026/2/6 15:47
 *
 */
@Mapper
public interface QrCodeConverter {
    QrCodeConverter INSTANCE = Mappers.getMapper(QrCodeConverter.class);

    QrCodeEntity requestToQrCode(CreateQrcodeRequest vo);

}
