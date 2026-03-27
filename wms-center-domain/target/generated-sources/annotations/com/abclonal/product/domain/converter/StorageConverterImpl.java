package com.abclonal.product.domain.converter;

import com.abclonal.product.api.domain.request.sys.StorageRequest;
import com.abclonal.product.dao.entity.Storage;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-27T10:16:06+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 1.8.0_482 (Private Build)"
)
public class StorageConverterImpl implements StorageConverter {

    @Override
    public Storage requestToEntity(StorageRequest request) {
        if ( request == null ) {
            return null;
        }

        Storage storage = new Storage();

        Map<String, Object> map = request.getParams();
        if ( map != null ) {
            storage.setParams( new LinkedHashMap<String, Object>( map ) );
        }
        storage.setStorageCode( request.getStorageCode() );
        storage.setStorageName( request.getStorageName() );
        storage.setWarehouseId( request.getWarehouseId() );
        storage.setLocationId( request.getLocationId() );
        storage.setStorageType( request.getStorageType() );
        storage.setCapacity( request.getCapacity() );
        storage.setIsEnabled( request.getIsEnabled() );
        storage.setRemark( request.getRemark() );

        return storage;
    }
}
