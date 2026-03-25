package com.abclonal.product.domain.converter;

import com.abclonal.product.api.domain.request.sys.WarehouseRequest;
import com.abclonal.product.dao.entity.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 仓库档案转换器
 *
 * @author backend
 * @since 2026-03-18
 */
@Mapper
public interface WarehouseConverter {

    WarehouseConverter INSTANCE = Mappers.getMapper(WarehouseConverter.class);

    /**
     * Request 转 Entity
     */
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "createTime", ignore = true),
        @Mapping(target = "createBy", ignore = true),
        @Mapping(target = "updateTime", ignore = true),
        @Mapping(target = "updateBy", ignore = true),
        @Mapping(target = "isdeleted", ignore = true)
    })
    Warehouse requestToEntity(WarehouseRequest request);
}
