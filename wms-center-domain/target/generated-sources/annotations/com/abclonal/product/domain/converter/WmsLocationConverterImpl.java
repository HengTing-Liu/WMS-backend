package com.abclonal.product.domain.converter;

import com.abclonal.product.api.domain.request.location.WmsLocationBatchCreateRequest;
import com.abclonal.product.api.domain.request.location.WmsLocationRequest;
import com.abclonal.product.api.domain.response.location.WmsLocationOccupancyResponse;
import com.abclonal.product.api.domain.response.location.WmsLocationResponse;
import com.abclonal.product.dao.entity.WmsLocation;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-26T10:45:22+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 1.8.0_482 (Private Build)"
)
public class WmsLocationConverterImpl implements WmsLocationConverter {

    @Override
    public WmsLocation requestToEntity(WmsLocationRequest request) {
        if ( request == null ) {
            return null;
        }

        WmsLocation wmsLocation = new WmsLocation();

        wmsLocation.setCreateBy( request.getCreateBy() );
        wmsLocation.setCreateTime( request.getCreateTime() );
        wmsLocation.setUpdateBy( request.getUpdateBy() );
        wmsLocation.setUpdateTime( request.getUpdateTime() );
        Map<String, Object> map = request.getParams();
        if ( map != null ) {
            wmsLocation.setParams( new LinkedHashMap<String, Object>( map ) );
        }
        wmsLocation.setId( request.getId() );
        wmsLocation.setParentId( request.getParentId() );
        wmsLocation.setLocationGrade( request.getLocationGrade() );
        wmsLocation.setLocationType( request.getLocationType() );
        wmsLocation.setLocationLevel( request.getLocationLevel() );
        wmsLocation.setLocationLevelCount( request.getLocationLevelCount() );
        wmsLocation.setInternalSerialNo( request.getInternalSerialNo() );
        wmsLocation.setInternalQuantity( request.getInternalQuantity() );
        wmsLocation.setLocationNo( request.getLocationNo() );
        wmsLocation.setLocationName( request.getLocationName() );
        wmsLocation.setWarehouseCode( request.getWarehouseCode() );
        wmsLocation.setParentName( request.getParentName() );
        wmsLocation.setStorageMode( request.getStorageMode() );
        wmsLocation.setSpecification( request.getSpecification() );
        wmsLocation.setIsUse( request.getIsUse() );
        wmsLocation.setLocationSortNo( request.getLocationSortNo() );
        wmsLocation.setLocationFullpathName( request.getLocationFullpathName() );
        wmsLocation.setCapacityTotal( request.getCapacityTotal() );
        wmsLocation.setCapacityUsed( request.getCapacityUsed() );
        wmsLocation.setIsDeleted( request.getIsDeleted() );
        wmsLocation.setRemarks( request.getRemarks() );

        return wmsLocation;
    }

    @Override
    public WmsLocation batchCreateRequestToEntity(WmsLocationBatchCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        WmsLocation wmsLocation = new WmsLocation();

        wmsLocation.setRemarks( request.getRemarks() );
        wmsLocation.setParentId( request.getParentId() );
        wmsLocation.setLocationGrade( request.getLocationGrade() );
        wmsLocation.setLocationType( request.getLocationType() );
        wmsLocation.setWarehouseCode( request.getWarehouseCode() );
        wmsLocation.setStorageMode( request.getStorageMode() );
        wmsLocation.setSpecification( request.getSpecification() );

        wmsLocation.setCapacityTotal( calculateCapacity(request.getSpecification()) );
        wmsLocation.setCapacityUsed( 0 );
        wmsLocation.setIsUse( 0 );
        wmsLocation.setIsDeleted( 0 );

        return wmsLocation;
    }

    @Override
    public WmsLocationResponse entityToResponse(WmsLocation entity) {
        if ( entity == null ) {
            return null;
        }

        WmsLocationResponse wmsLocationResponse = new WmsLocationResponse();

        wmsLocationResponse.setCreateBy( entity.getCreateBy() );
        wmsLocationResponse.setCreateTime( entity.getCreateTime() );
        wmsLocationResponse.setUpdateBy( entity.getUpdateBy() );
        wmsLocationResponse.setUpdateTime( entity.getUpdateTime() );
        wmsLocationResponse.setRemark( entity.getRemark() );
        wmsLocationResponse.setId( entity.getId() );
        wmsLocationResponse.setParentId( entity.getParentId() );
        wmsLocationResponse.setLocationGrade( entity.getLocationGrade() );
        wmsLocationResponse.setLocationType( entity.getLocationType() );
        wmsLocationResponse.setLocationLevel( entity.getLocationLevel() );
        wmsLocationResponse.setLocationLevelCount( entity.getLocationLevelCount() );
        wmsLocationResponse.setInternalSerialNo( entity.getInternalSerialNo() );
        wmsLocationResponse.setInternalQuantity( entity.getInternalQuantity() );
        wmsLocationResponse.setLocationNo( entity.getLocationNo() );
        wmsLocationResponse.setLocationName( entity.getLocationName() );
        wmsLocationResponse.setWarehouseCode( entity.getWarehouseCode() );
        wmsLocationResponse.setParentName( entity.getParentName() );
        wmsLocationResponse.setStorageMode( entity.getStorageMode() );
        wmsLocationResponse.setSpecification( entity.getSpecification() );
        wmsLocationResponse.setIsUse( entity.getIsUse() );
        wmsLocationResponse.setLocationSortNo( entity.getLocationSortNo() );
        wmsLocationResponse.setLocationFullpathName( entity.getLocationFullpathName() );
        wmsLocationResponse.setCapacityTotal( entity.getCapacityTotal() );
        wmsLocationResponse.setCapacityUsed( entity.getCapacityUsed() );
        wmsLocationResponse.setRemarks( entity.getRemarks() );
        wmsLocationResponse.setGridConfig( entity.getGridConfig() );

        return wmsLocationResponse;
    }

    @Override
    public WmsLocationResponse entityToResponseWithOccupancy(WmsLocation entity) {
        if ( entity == null ) {
            return null;
        }

        WmsLocationResponse wmsLocationResponse = new WmsLocationResponse();

        wmsLocationResponse.setCreateBy( entity.getCreateBy() );
        wmsLocationResponse.setCreateTime( entity.getCreateTime() );
        wmsLocationResponse.setUpdateBy( entity.getUpdateBy() );
        wmsLocationResponse.setUpdateTime( entity.getUpdateTime() );
        wmsLocationResponse.setRemark( entity.getRemark() );
        wmsLocationResponse.setId( entity.getId() );
        wmsLocationResponse.setParentId( entity.getParentId() );
        wmsLocationResponse.setLocationGrade( entity.getLocationGrade() );
        wmsLocationResponse.setLocationType( entity.getLocationType() );
        wmsLocationResponse.setLocationLevel( entity.getLocationLevel() );
        wmsLocationResponse.setLocationLevelCount( entity.getLocationLevelCount() );
        wmsLocationResponse.setInternalSerialNo( entity.getInternalSerialNo() );
        wmsLocationResponse.setInternalQuantity( entity.getInternalQuantity() );
        wmsLocationResponse.setLocationNo( entity.getLocationNo() );
        wmsLocationResponse.setLocationName( entity.getLocationName() );
        wmsLocationResponse.setWarehouseCode( entity.getWarehouseCode() );
        wmsLocationResponse.setParentName( entity.getParentName() );
        wmsLocationResponse.setStorageMode( entity.getStorageMode() );
        wmsLocationResponse.setSpecification( entity.getSpecification() );
        wmsLocationResponse.setIsUse( entity.getIsUse() );
        wmsLocationResponse.setLocationSortNo( entity.getLocationSortNo() );
        wmsLocationResponse.setLocationFullpathName( entity.getLocationFullpathName() );
        wmsLocationResponse.setCapacityTotal( entity.getCapacityTotal() );
        wmsLocationResponse.setCapacityUsed( entity.getCapacityUsed() );
        wmsLocationResponse.setRemarks( entity.getRemarks() );
        wmsLocationResponse.setGridConfig( entity.getGridConfig() );

        wmsLocationResponse.setOccupancyRate( calculateOccupancyRate(entity.getCapacityUsed(), entity.getCapacityTotal()) );

        return wmsLocationResponse;
    }

    @Override
    public WmsLocationOccupancyResponse entityToOccupancyResponse(WmsLocation entity) {
        if ( entity == null ) {
            return null;
        }

        WmsLocationOccupancyResponse wmsLocationOccupancyResponse = new WmsLocationOccupancyResponse();

        wmsLocationOccupancyResponse.setLocationId( entity.getId() );
        wmsLocationOccupancyResponse.setLocationNo( entity.getLocationNo() );
        wmsLocationOccupancyResponse.setLocationName( entity.getLocationName() );
        wmsLocationOccupancyResponse.setWarehouseCode( entity.getWarehouseCode() );
        wmsLocationOccupancyResponse.setCapacityTotal( entity.getCapacityTotal() );
        wmsLocationOccupancyResponse.setCapacityUsed( entity.getCapacityUsed() );
        wmsLocationOccupancyResponse.setStorageMode( entity.getStorageMode() );
        wmsLocationOccupancyResponse.setLocationLevel( entity.getLocationLevel() );

        wmsLocationOccupancyResponse.setCapacityFree( calculateFreeCapacity(entity.getCapacityTotal(), entity.getCapacityUsed()) );
        wmsLocationOccupancyResponse.setOccupancyRate( calculateOccupancyRate(entity.getCapacityUsed(), entity.getCapacityTotal()) );

        return wmsLocationOccupancyResponse;
    }

    @Override
    public List<WmsLocationResponse> entityListToResponseList(List<WmsLocation> entities) {
        if ( entities == null ) {
            return null;
        }

        List<WmsLocationResponse> list = new ArrayList<WmsLocationResponse>( entities.size() );
        for ( WmsLocation wmsLocation : entities ) {
            list.add( entityToResponse( wmsLocation ) );
        }

        return list;
    }

    @Override
    public List<WmsLocationResponse> entityListToResponseListWithOccupancy(List<WmsLocation> entities) {
        if ( entities == null ) {
            return null;
        }

        List<WmsLocationResponse> list = new ArrayList<WmsLocationResponse>( entities.size() );
        for ( WmsLocation wmsLocation : entities ) {
            list.add( entityToResponseWithOccupancy( wmsLocation ) );
        }

        return list;
    }
}
