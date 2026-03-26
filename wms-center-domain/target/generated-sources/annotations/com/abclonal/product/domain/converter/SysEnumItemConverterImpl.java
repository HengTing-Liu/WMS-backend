package com.abclonal.product.domain.converter;

import com.abclonal.product.api.domain.request.sys.SysEnumItemRequest;
import com.abclonal.product.api.domain.response.sys.SysEnumItemResponse;
import com.abclonal.product.dao.entity.SysEnumItem;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-26T10:45:22+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 1.8.0_482 (Private Build)"
)
public class SysEnumItemConverterImpl implements SysEnumItemConverter {

    @Override
    public SysEnumItem requestToSysEnumItem(SysEnumItemRequest vo) {
        if ( vo == null ) {
            return null;
        }

        SysEnumItem sysEnumItem = new SysEnumItem();

        sysEnumItem.setCreateBy( vo.getCreateBy() );
        sysEnumItem.setCreateTime( vo.getCreateTime() );
        sysEnumItem.setUpdateBy( vo.getUpdateBy() );
        sysEnumItem.setUpdateTime( vo.getUpdateTime() );
        Map<String, Object> map = vo.getParams();
        if ( map != null ) {
            sysEnumItem.setParams( new LinkedHashMap<String, Object>( map ) );
        }
        sysEnumItem.setId( vo.getId() );
        sysEnumItem.setEnumCode( vo.getEnumCode() );
        sysEnumItem.setItemKey( vo.getItemKey() );
        sysEnumItem.setItemValue( vo.getItemValue() );
        sysEnumItem.setItemDesc( vo.getItemDesc() );
        sysEnumItem.setSortOrder( vo.getSortOrder() );
        sysEnumItem.setIsDefault( vo.getIsDefault() );
        sysEnumItem.setIsEnabled( vo.getIsEnabled() );

        return sysEnumItem;
    }

    @Override
    public SysEnumItemResponse SysEnumItemToResponse(SysEnumItem sysEnumItem) {
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
}
