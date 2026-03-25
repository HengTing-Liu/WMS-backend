package com.abclonal.product.domain.converter;

import com.abclonal.product.api.domain.request.location.WmsLocationBatchCreateRequest;
import com.abclonal.product.api.domain.request.location.WmsLocationRequest;
import com.abclonal.product.api.domain.response.location.WmsLocationOccupancyResponse;
import com.abclonal.product.api.domain.response.location.WmsLocationResponse;
import com.abclonal.product.dao.entity.WmsLocation;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 库位档案转换器
 *
 * @author wms
 * @since 2026-03-14
 */
@Mapper
public interface WmsLocationConverter {

    WmsLocationConverter INSTANCE = Mappers.getMapper(WmsLocationConverter.class);

    // ==================== Request -> Entity ====================

    /**
     * Request 转 Entity（基础转换）
     */
    @Mappings({
        @Mapping(target = "children", ignore = true)
    })
    WmsLocation requestToEntity(WmsLocationRequest request);

    /**
     * 批量创建请求转 Entity
     */
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "internalSerialNo", ignore = true),
        @Mapping(target = "internalQuantity", ignore = true),
        @Mapping(target = "locationNo", ignore = true),
        @Mapping(target = "locationName", ignore = true),
        @Mapping(target = "locationSortNo", ignore = true),
        @Mapping(target = "locationFullpathName", ignore = true),
        @Mapping(target = "capacityTotal", expression = "java(calculateCapacity(request.getSpecification()))"),
        @Mapping(target = "capacityUsed", constant = "0"),
        @Mapping(target = "isUse", constant = "0"),
        @Mapping(target = "isDeleted", constant = "0"),
        @Mapping(target = "children", ignore = true),
        @Mapping(target = "createBy", ignore = true),
        @Mapping(target = "createTime", ignore = true),
        @Mapping(target = "updateBy", ignore = true),
        @Mapping(target = "updateTime", ignore = true),
        @Mapping(target = "remarks", source = "remarks")
    })
    WmsLocation batchCreateRequestToEntity(WmsLocationBatchCreateRequest request);

    // ==================== Entity -> Response ====================

    /**
     * Entity 转 Response（基础转换）
     */
    @Named("entityToResponse")
    @Mappings({
        @Mapping(target = "occupancyRate", ignore = true),
        @Mapping(target = "children", ignore = true)
    })
    WmsLocationResponse entityToResponse(WmsLocation entity);

    /**
     * Entity 转 Response（携带占用率）
     */
    @Named("entityToResponseWithOccupancy")
    @Mappings({
        @Mapping(target = "occupancyRate", expression = "java(calculateOccupancyRate(entity.getCapacityUsed(), entity.getCapacityTotal()))"),
        @Mapping(target = "children", ignore = true)
    })
    WmsLocationResponse entityToResponseWithOccupancy(WmsLocation entity);

    /**
     * Entity 转占用率统计 Response
     */
    @Mappings({
        @Mapping(target = "locationId", source = "id"),
        @Mapping(target = "capacityFree", expression = "java(calculateFreeCapacity(entity.getCapacityTotal(), entity.getCapacityUsed()))"),
        @Mapping(target = "occupancyRate", expression = "java(calculateOccupancyRate(entity.getCapacityUsed(), entity.getCapacityTotal()))")
    })
    WmsLocationOccupancyResponse entityToOccupancyResponse(WmsLocation entity);

    /**
     * Entity 列表转 Response 列表（基础转换）
     */
    @Named("entityListToResponseList")
    @IterableMapping(qualifiedByName = "entityToResponse")
    List<WmsLocationResponse> entityListToResponseList(List<WmsLocation> entities);

    /**
     * Entity 列表转 Response 列表（携带占用率）
     */
    @Named("entityListToResponseListWithOccupancy")
    @IterableMapping(qualifiedByName = "entityToResponseWithOccupancy")
    List<WmsLocationResponse> entityListToResponseListWithOccupancy(List<WmsLocation> entities);

    // ==================== 默认方法（计算辅助） ====================

    /**
     * 计算占用率
     */
    @Named("calculateOccupancyRate")
    default BigDecimal calculateOccupancyRate(Integer used, Integer total) {
        if (total == null || total == 0) {
            return BigDecimal.ZERO;
        }
        int usedValue = used != null ? used : 0;
        return new BigDecimal(usedValue)
                .multiply(new BigDecimal(100))
                .divide(new BigDecimal(total), 2, RoundingMode.HALF_UP);
    }

    /**
     * 计算空闲容量
     */
    @Named("calculateFreeCapacity")
    default Integer calculateFreeCapacity(Integer total, Integer used) {
        int totalValue = total != null ? total : 0;
        int usedValue = used != null ? used : 0;
        return totalValue - usedValue;
    }

    /**
     * 根据规格计算容量
     */
    @Named("calculateCapacity")
    default Integer calculateCapacity(String specification) {
        if (specification == null || !specification.contains("x")) {
            return 0;
        }
        try {
            String[] dims = specification.toLowerCase().split("x");
            if (dims.length == 2) {
                int row = Integer.parseInt(dims[0].trim());
                int col = Integer.parseInt(dims[1].trim());
                return row * col;
            }
        } catch (NumberFormatException e) {
            // 解析失败返回0
        }
        return 0;
    }
}
