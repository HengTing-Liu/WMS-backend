package com.abclonal.product.domain.converter;

import com.abclonal.product.api.domain.request.sys.WarehouseRequest;
import com.abclonal.product.dao.entity.Warehouse;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-26T10:45:22+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 1.8.0_482 (Private Build)"
)
public class WarehouseConverterImpl implements WarehouseConverter {

    @Override
    public Warehouse requestToEntity(WarehouseRequest request) {
        if ( request == null ) {
            return null;
        }

        Warehouse warehouse = new Warehouse();

        warehouse.setRemark( request.getRemark() );
        Map<String, Object> map = request.getParams();
        if ( map != null ) {
            warehouse.setParams( new LinkedHashMap<String, Object>( map ) );
        }
        warehouse.setWarehouseCode( request.getWarehouseCode() );
        warehouse.setWarehouseName( request.getWarehouseName() );
        warehouse.setTemperatureZone( request.getTemperatureZone() );
        warehouse.setQualityZone( request.getQualityZone() );
        warehouse.setEmployeeCode( request.getEmployeeCode() );
        warehouse.setEmployeeName( request.getEmployeeName() );
        warehouse.setDeptCode( request.getDeptCode() );
        warehouse.setDeptNameFullPath( request.getDeptNameFullPath() );
        warehouse.setCompany( request.getCompany() );
        warehouse.setIsEnabled( request.getIsEnabled() );

        return warehouse;
    }
}
