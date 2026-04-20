package com.abtk.product.api.domain.request.location;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 层级批量创建库位请求
 * 支持多级分区 + 容器 + 孔位的一次性批量创建
 */
@Data
@Schema(description = "层级批量创建库位请求")
public class WmsLocationHierarchyCreateRequest {

    /**
     * 父级ID
     */
    @NotNull(message = "父级ID不能为空")
    @Schema(description = "父级ID", required = true)
    private Long parentId;

    /**
     * 仓库编码
     */
    @NotBlank(message = "仓库编码不能为空")
    @Schema(description = "仓库编码", required = true)
    private String warehouseCode;

    /**
     * 分区层级配置（按顺序，如：层 → 架 → 行）
     */
    @NotEmpty(message = "分区层级配置不能为空")
    @Valid
    @Schema(description = "分区层级配置列表", required = true)
    private List<LevelConfig> levels;

    /**
     * 容器配置（最底层分区下创建的容器及孔位）
     */
    @Valid
    @Schema(description = "容器配置")
    private ContainerConfig container;
}
