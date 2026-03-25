package com.abclonal.product.domain.converter;

import com.abclonal.product.api.domain.request.sys.SysEnumDefineRequest;
import com.abclonal.product.api.domain.response.sys.SysEnumDefineResponse;
import com.abclonal.product.dao.entity.SysEnumDefine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
public interface SysEnumDefineConverter {
    SysEnumDefineConverter INSTANCE = Mappers.getMapper(SysEnumDefineConverter.class);

    SysEnumDefine requestToSysEnumDefine(SysEnumDefineRequest vo);

    SysEnumDefineResponse SysEnumDefineToResponse(SysEnumDefine sysEnumDefine);

}

