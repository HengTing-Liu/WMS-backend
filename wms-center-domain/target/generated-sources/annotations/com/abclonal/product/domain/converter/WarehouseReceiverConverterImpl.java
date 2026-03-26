package com.abclonal.product.domain.converter;

import com.abclonal.product.api.domain.request.sys.WarehouseReceiverRequest;
import com.abclonal.product.dao.entity.WarehouseReceiver;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-26T10:45:22+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 1.8.0_482 (Private Build)"
)
public class WarehouseReceiverConverterImpl implements WarehouseReceiverConverter {

    @Override
    public WarehouseReceiver requestToEntity(WarehouseReceiverRequest request) {
        if ( request == null ) {
            return null;
        }

        WarehouseReceiver warehouseReceiver = new WarehouseReceiver();

        warehouseReceiver.setRemark( request.getRemark() );
        Map<String, Object> map = request.getParams();
        if ( map != null ) {
            warehouseReceiver.setParams( new LinkedHashMap<String, Object>( map ) );
        }
        warehouseReceiver.setWarehouseCode( request.getWarehouseCode() );
        warehouseReceiver.setConsignee( request.getConsignee() );
        warehouseReceiver.setPhoneNumber( request.getPhoneNumber() );
        warehouseReceiver.setCountry( request.getCountry() );
        warehouseReceiver.setProvince( request.getProvince() );
        warehouseReceiver.setCity( request.getCity() );
        warehouseReceiver.setDistrict( request.getDistrict() );
        warehouseReceiver.setDetailedAddress( request.getDetailedAddress() );
        warehouseReceiver.setPostalCode( request.getPostalCode() );
        warehouseReceiver.setIsDefault( request.getIsDefault() );

        return warehouseReceiver;
    }
}
