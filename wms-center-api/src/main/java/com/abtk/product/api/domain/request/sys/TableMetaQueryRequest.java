package com.abtk.product.api.domain.request.sys;

import com.abtk.product.api.domain.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 表元数据查询请求
 */
@Data
@Schema(description = "表元数据查询请求")
public class TableMetaQueryRequest extends BaseRequest {

    @Schema(description = "表标识")
    private String tableCode;

    @Schema(description = "表名称(模糊查询)")
    private String tableName;

    @Schema(description = "所属模块")
    private String module;

    @Schema(description = "状态(0-禁用,1-启用)")
    private Integer status;
}
