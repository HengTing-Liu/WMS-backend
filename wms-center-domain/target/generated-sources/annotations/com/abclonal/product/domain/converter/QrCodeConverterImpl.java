package com.abclonal.product.domain.converter;

import com.abclonal.product.api.domain.request.inv.CreateQrcodeRequest;
import com.abclonal.product.dao.entity.QrCodeEntity;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-26T10:45:22+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 1.8.0_482 (Private Build)"
)
public class QrCodeConverterImpl implements QrCodeConverter {

    @Override
    public QrCodeEntity requestToQrCode(CreateQrcodeRequest vo) {
        if ( vo == null ) {
            return null;
        }

        QrCodeEntity qrCodeEntity = new QrCodeEntity();

        qrCodeEntity.setCatalogno( vo.getCatalogno() );
        qrCodeEntity.setProductname( vo.getProductname() );
        qrCodeEntity.setBatchid( vo.getBatchid() );
        qrCodeEntity.setSize( vo.getSize() );
        qrCodeEntity.setUnit( vo.getUnit() );
        qrCodeEntity.setCreateBy( vo.getCreateBy() );
        qrCodeEntity.setCreateTime( vo.getCreateTime() );
        qrCodeEntity.setUpdateBy( vo.getUpdateBy() );
        qrCodeEntity.setUpdateTime( vo.getUpdateTime() );

        return qrCodeEntity;
    }
}
