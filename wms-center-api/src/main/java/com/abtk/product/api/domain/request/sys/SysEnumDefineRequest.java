package com.abtk.product.api.domain.request.sys;

import com.abtk.product.api.domain.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 枚举定义表(SysEnumDefine)入参
 *
 * @author lht
 * @since 2026-03-06 14:36:20
 */
@Data
@Schema(description = "SysEnumDefine请求入参")
public class SysEnumDefineRequest extends BaseRequest {
    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 枚举编码，如：container_type
     */
    @Schema(description = "枚举编码，如：container_type")
    private String enumCode;

    /**
     * 枚举名称，如：容器类型
     */
    @Schema(description = "枚举名称，如：容器类型")
    private String enumName;

    /**
     * 枚举描述
     */
    @Schema(description = "枚举描述")
    private String enumDesc;

    /**
     * 分类编码，如：LOCATION
     */
    @Schema(description = "分类编码，如：LOCATION")
    private String categoryCode;

    /**
     * 分类名称，如：库位相关
     */
    @Schema(description = "分类名称，如：库位相关")
    private String categoryName;

    /**
     * 是否启用
     */
    @Schema(description = "是否启用")
    private Integer isEnabled;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sortOrder;
}
