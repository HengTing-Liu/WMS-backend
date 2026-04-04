package com.abtk.product.domain.converter;

import com.abtk.product.api.domain.request.sys.StorageRequest;
import com.abtk.product.dao.entity.Storage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 库区档案转换器
 * WMS0050 库区管理
 *
 * @author backend
 * @since 2026-03-26
 */
@Mapper
public interface StorageConverter {

    StorageConverter INSTANCE = Mappers.getMapper(StorageConverter.class);

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
    Storage requestToEntity(StorageRequest request);
}
