package com.abclonal.product.domain.converter;

import com.abclonal.product.api.domain.request.sys.SysEnumDefineRequest;
import com.abclonal.product.api.domain.response.sys.SysEnumDefineResponse;
import com.abclonal.product.dao.entity.SysEnumDefine;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-27T10:16:06+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 1.8.0_482 (Private Build)"
)
public class SysEnumDefineConverterImpl implements SysEnumDefineConverter {

    @Override
    public SysEnumDefine requestToSysEnumDefine(SysEnumDefineRequest vo) {
        if ( vo == null ) {
            return null;
        }

        SysEnumDefine sysEnumDefine = new SysEnumDefine();

        sysEnumDefine.setCreateBy( vo.getCreateBy() );
        sysEnumDefine.setCreateTime( vo.getCreateTime() );
        sysEnumDefine.setUpdateBy( vo.getUpdateBy() );
        sysEnumDefine.setUpdateTime( vo.getUpdateTime() );
        Map<String, Object> map = vo.getParams();
        if ( map != null ) {
            sysEnumDefine.setParams( new LinkedHashMap<String, Object>( map ) );
        }
        sysEnumDefine.setId( vo.getId() );
        sysEnumDefine.setEnumCode( vo.getEnumCode() );
        sysEnumDefine.setEnumName( vo.getEnumName() );
        sysEnumDefine.setEnumDesc( vo.getEnumDesc() );
        sysEnumDefine.setCategoryCode( vo.getCategoryCode() );
        sysEnumDefine.setCategoryName( vo.getCategoryName() );
        sysEnumDefine.setIsEnabled( vo.getIsEnabled() );
        sysEnumDefine.setSortOrder( vo.getSortOrder() );

        return sysEnumDefine;
    }

    @Override
    public SysEnumDefineResponse SysEnumDefineToResponse(SysEnumDefine sysEnumDefine) {
        if ( sysEnumDefine == null ) {
            return null;
        }

        SysEnumDefineResponse sysEnumDefineResponse = new SysEnumDefineResponse();

        sysEnumDefineResponse.setCreateBy( sysEnumDefine.getCreateBy() );
        sysEnumDefineResponse.setCreateTime( sysEnumDefine.getCreateTime() );
        sysEnumDefineResponse.setUpdateBy( sysEnumDefine.getUpdateBy() );
        sysEnumDefineResponse.setUpdateTime( sysEnumDefine.getUpdateTime() );
        sysEnumDefineResponse.setRemark( sysEnumDefine.getRemark() );
        sysEnumDefineResponse.setId( sysEnumDefine.getId() );
        sysEnumDefineResponse.setEnumCode( sysEnumDefine.getEnumCode() );
        sysEnumDefineResponse.setEnumName( sysEnumDefine.getEnumName() );
        sysEnumDefineResponse.setEnumDesc( sysEnumDefine.getEnumDesc() );
        sysEnumDefineResponse.setCategoryCode( sysEnumDefine.getCategoryCode() );
        sysEnumDefineResponse.setCategoryName( sysEnumDefine.getCategoryName() );
        sysEnumDefineResponse.setIsEnabled( sysEnumDefine.getIsEnabled() );
        sysEnumDefineResponse.setSortOrder( sysEnumDefine.getSortOrder() );

        return sysEnumDefineResponse;
    }
}
