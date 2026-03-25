package com.abclonal.product.domain.converter;

import com.abclonal.product.api.domain.request.sys.SysEnumItemRequest;
import com.abclonal.product.api.domain.response.sys.SysEnumItemResponse;
import com.abclonal.product.dao.entity.SysEnumItem;
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
public interface SysEnumItemConverter {
    SysEnumItemConverter INSTANCE = Mappers.getMapper(SysEnumItemConverter.class);

    SysEnumItem requestToSysEnumItem(SysEnumItemRequest vo);

    SysEnumItemResponse SysEnumItemToResponse(SysEnumItem sysEnumItem);

}

