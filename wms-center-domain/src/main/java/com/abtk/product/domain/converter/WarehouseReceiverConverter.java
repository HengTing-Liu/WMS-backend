package com.abtk.product.domain.converter;

import com.abtk.product.api.domain.request.sys.WarehouseReceiverRequest;
import com.abtk.product.dao.entity.WarehouseReceiver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 仓库收货信息转换器
 *
 * @author backend
 * @since 2026-03-18
 */
@Mapper
public interface WarehouseReceiverConverter {

    WarehouseReceiverConverter INSTANCE = Mappers.getMapper(WarehouseReceiverConverter.class);

    // ==================== Request -> Entity ====================

    /**
     * Request 转 Entity
     */
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "createTime", ignore = true),
        @Mapping(target = "createBy", ignore = true),
        @Mapping(target = "updateTime", ignore = true),
        @Mapping(target = "updateBy", ignore = true),
        @Mapping(target = "isDeleted", ignore = true)
    })
    WarehouseReceiver requestToEntity(WarehouseReceiverRequest request);

    // ==================== Entity -> Response ====================

    /**
     * Entity 转 Response（如有需要可扩展）
     */
    // WarehouseReceiverResponse entityToResponse(WarehouseReceiver entity);
}
