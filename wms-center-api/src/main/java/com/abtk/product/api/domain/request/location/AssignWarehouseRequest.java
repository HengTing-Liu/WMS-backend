package com.abtk.product.api.domain.request.location;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 分配仓库请求DTO
 *
 * @author wms
 * @since 2026-04-15
 */
@Data
@Schema(description = "分配仓库请求参数")
public class AssignWarehouseRequest {

    /**
     * 存储类型/存储分区ID（用于获取原仓库和温区信息）
     */
    @Schema(description = "存储类型/存储分区ID")
    private Long locationId;

    /**
     * 待分配的存储容器ID列表
     */
    @NotEmpty(message = "存储容器ID列表不能为空")
    @Schema(description = "待分配的存储容器ID列表")
    private List<Long> containerIds;

    /**
     * 目标仓库编码
     */
    @NotBlank(message = "目标仓库编码不能为空")
    @Schema(description = "目标仓库编码")
    private String targetWarehouseCode;
}
