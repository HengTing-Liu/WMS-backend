package com.abtk.product.api.domain.response.system;

import com.abtk.product.api.domain.response.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 表元数据响应
 *
 * @author backend
 * @since 2026-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "表元数据响应")
public class WmsTableMetaResponse extends BaseResponse {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 表编码
     */
    @Schema(description = "表编码")
    private String tableCode;

    /**
     * 表名称
     */
    @Schema(description = "表名称")
    private String tableName;

    /**
     * 所属模块
     */
    @Schema(description = "所属模块")
    private String module;

    /**
     * 实体类名
     */
    @Schema(description = "实体类名")
    private String entityClass;

    /**
     * 服务类名
     */
    @Schema(description = "服务类名")
    private String serviceClass;

    /**
     * 权限标识
     */
    @Schema(description = "权限标识")
    private String permissionCode;

    /**
     * 默认页大小
     */
    @Schema(description = "默认页大小")
    private Integer pageSize;

    /**
     * 是否树形：0=否，1=是
     */
    @Schema(description = "是否树形：0=否，1=是")
    private Integer isTree;

    /**
     * 状态：0=禁用，1=启用
     */
    @Schema(description = "状态：0=禁用，1=启用")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

    /**
     * 是否有数据权限：0=否，1=是
     */
    @Schema(description = "是否有数据权限：0=否，1=是")
    private Integer hasDataPermission;

    /**
     * 权限字段
     */
    @Schema(description = "权限字段")
    private String permissionField;

    /**
     * 权限范围
     */
    @Schema(description = "权限范围")
    private String permissionScope;

    /**
     * 字段数量
     */
    @Schema(description = "字段数量")
    private Integer fieldCount;
}
