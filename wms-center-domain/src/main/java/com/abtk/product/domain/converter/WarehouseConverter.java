package com.abtk.product.domain.converter;

import com.abtk.product.api.domain.request.sys.WarehouseRequest;
import com.abtk.product.api.domain.response.sys.WarehouseResponse;
import com.abtk.product.dao.entity.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;

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
        @Mapping(target = "isdeleted", ignore = true),
        @Mapping(target = "searchValue", ignore = true)
    })
    Warehouse requestToEntity(WarehouseRequest request);

    /**
     * Entity 转 Response（用于导出）
     */
    default WarehouseResponse entityToResponse(Warehouse entity) {
        if (entity == null) {
            return null;
        }
        WarehouseResponse response = new WarehouseResponse();
        response.setId(entity.getId());
        response.setWarehouseCode(entity.getWarehouseCode());
        response.setWarehouseName(entity.getWarehouseName());
        response.setTemperatureZone(entity.getTemperatureZone());
        response.setCompany(entity.getCompany());
        response.setStatus(entity.getIsEnabled() == 1 ? "ENABLED" : "DISABLED");
        if (entity.getCreateTime() != null) {
            response.setGmtCreate(LocalDateTime.ofInstant(entity.getCreateTime().toInstant(), ZoneId.systemDefault()));
        }
        return response;
    }
}
