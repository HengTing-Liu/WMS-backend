package com.abclonal.product.domain.converter;

import com.abclonal.product.api.domain.request.sys.SysEnumDefineRequest;
import com.abclonal.product.api.domain.request.sys.SysEnumDefineWithItemsRequest;
import com.abclonal.product.api.domain.request.sys.SysEnumItemRequest;
import com.abclonal.product.api.domain.response.sys.SysEnumDefineWithItemsResponse;
import com.abclonal.product.api.domain.response.sys.SysEnumItemResponse;
import com.abclonal.product.dao.entity.SysEnumDefine;
import com.abclonal.product.dao.entity.SysEnumItem;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-27T10:16:06+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 1.8.0_482 (Private Build)"
)
public class SysEnumDefineComplexConverterImpl implements SysEnumDefineComplexConverter {

    @Override
    public SysEnumDefine toEntity(SysEnumDefineRequest request) {
        if ( request == null ) {
            return null;
        }

        SysEnumDefine sysEnumDefine = new SysEnumDefine();

        Map<String, Object> map = request.getParams();
        if ( map != null ) {
            sysEnumDefine.setParams( new LinkedHashMap<String, Object>( map ) );
        }
        sysEnumDefine.setEnumCode( request.getEnumCode() );
        sysEnumDefine.setEnumName( request.getEnumName() );
        sysEnumDefine.setEnumDesc( request.getEnumDesc() );
        sysEnumDefine.setCategoryCode( request.getCategoryCode() );
        sysEnumDefine.setCategoryName( request.getCategoryName() );
        sysEnumDefine.setIsEnabled( request.getIsEnabled() );
        sysEnumDefine.setSortOrder( request.getSortOrder() );

        return sysEnumDefine;
    }

    @Override
    public SysEnumDefine toEntity(SysEnumDefineWithItemsRequest request) {
        if ( request == null ) {
            return null;
        }

        SysEnumDefine sysEnumDefine = new SysEnumDefine();

        Map<String, Object> map = request.getParams();
        if ( map != null ) {
            sysEnumDefine.setParams( new LinkedHashMap<String, Object>( map ) );
        }

        return sysEnumDefine;
    }

    @Override
    public List<SysEnumItem> toItemEntityList(List<SysEnumItemRequest> requests) {
        if ( requests == null ) {
            return null;
        }

        List<SysEnumItem> list = new ArrayList<SysEnumItem>( requests.size() );
        for ( SysEnumItemRequest sysEnumItemRequest : requests ) {
            list.add( toItemEntity( sysEnumItemRequest ) );
        }

        return list;
    }

    @Override
    public SysEnumItem toItemEntity(SysEnumItemRequest request) {
        if ( request == null ) {
            return null;
        }

        SysEnumItem sysEnumItem = new SysEnumItem();

        Map<String, Object> map = request.getParams();
        if ( map != null ) {
            sysEnumItem.setParams( new LinkedHashMap<String, Object>( map ) );
        }
        sysEnumItem.setItemKey( request.getItemKey() );
        sysEnumItem.setItemValue( request.getItemValue() );
        sysEnumItem.setItemDesc( request.getItemDesc() );
        sysEnumItem.setSortOrder( request.getSortOrder() );
        sysEnumItem.setIsDefault( request.getIsDefault() );
        sysEnumItem.setIsEnabled( request.getIsEnabled() );

        return sysEnumItem;
    }

    @Override
    public SysEnumDefineWithItemsResponse toResponse(SysEnumDefine enumDefine, List<SysEnumItem> enumItems) {
        if ( enumDefine == null && enumItems == null ) {
            return null;
        }

        SysEnumDefineWithItemsResponse sysEnumDefineWithItemsResponse = new SysEnumDefineWithItemsResponse();

        if ( enumDefine != null ) {
            sysEnumDefineWithItemsResponse.setEnumDefineId( enumDefine.getId() );
            sysEnumDefineWithItemsResponse.setEnumCode( enumDefine.getEnumCode() );
            sysEnumDefineWithItemsResponse.setEnumName( enumDefine.getEnumName() );
            sysEnumDefineWithItemsResponse.setCreateBy( enumDefine.getCreateBy() );
            sysEnumDefineWithItemsResponse.setCreateTime( enumDefine.getCreateTime() );
            sysEnumDefineWithItemsResponse.setUpdateBy( enumDefine.getUpdateBy() );
            sysEnumDefineWithItemsResponse.setUpdateTime( enumDefine.getUpdateTime() );
            sysEnumDefineWithItemsResponse.setRemark( enumDefine.getRemark() );
        }
        sysEnumDefineWithItemsResponse.setEnumItems( sysEnumItemListToSysEnumItemResponseList( enumItems ) );

        return sysEnumDefineWithItemsResponse;
    }

    protected SysEnumItemResponse sysEnumItemToSysEnumItemResponse(SysEnumItem sysEnumItem) {
        if ( sysEnumItem == null ) {
            return null;
        }

        SysEnumItemResponse sysEnumItemResponse = new SysEnumItemResponse();

        sysEnumItemResponse.setCreateBy( sysEnumItem.getCreateBy() );
        sysEnumItemResponse.setCreateTime( sysEnumItem.getCreateTime() );
        sysEnumItemResponse.setUpdateBy( sysEnumItem.getUpdateBy() );
        sysEnumItemResponse.setUpdateTime( sysEnumItem.getUpdateTime() );
        sysEnumItemResponse.setRemark( sysEnumItem.getRemark() );
        sysEnumItemResponse.setId( sysEnumItem.getId() );
        sysEnumItemResponse.setEnumCode( sysEnumItem.getEnumCode() );
        sysEnumItemResponse.setItemKey( sysEnumItem.getItemKey() );
        sysEnumItemResponse.setItemValue( sysEnumItem.getItemValue() );
        sysEnumItemResponse.setItemDesc( sysEnumItem.getItemDesc() );
        sysEnumItemResponse.setSortOrder( sysEnumItem.getSortOrder() );
        sysEnumItemResponse.setIsDefault( sysEnumItem.getIsDefault() );
        sysEnumItemResponse.setIsEnabled( sysEnumItem.getIsEnabled() );

        return sysEnumItemResponse;
    }

    protected List<SysEnumItemResponse> sysEnumItemListToSysEnumItemResponseList(List<SysEnumItem> list) {
        if ( list == null ) {
            return null;
        }

        List<SysEnumItemResponse> list1 = new ArrayList<SysEnumItemResponse>( list.size() );
        for ( SysEnumItem sysEnumItem : list ) {
            list1.add( sysEnumItemToSysEnumItemResponse( sysEnumItem ) );
        }

        return list1;
    }
}
