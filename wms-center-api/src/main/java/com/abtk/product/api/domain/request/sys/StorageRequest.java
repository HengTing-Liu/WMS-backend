package com.abtk.product.api.domain.request.sys;

import com.abtk.product.api.domain.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 库区档案请求入参
 * WMS0050 库区管理
 *
 * @author backend
 * @since 2026-03-26
 */
@Data
@Schema(description = "库区档案请求入参")
public class StorageRequest extends BaseRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 库区编码
     */
    @NotBlank(message = "库区编码不能为空")
    @Size(max = 50, message = "库区编码长度不能超过50")
    @Schema(description = "库区编码")
    private String storageCode;

    /**
     * 库区名称
     */
    @NotBlank(message = "库区名称不能为空")
    @Size(max = 100, message = "库区名称长度不能超过100")
    @Schema(description = "库区名称")
    private String storageName;

    /**
     * 仓库ID
     */
    @Schema(description = "仓库ID")
    private Long warehouseId;

    /**
     * 库位ID
     */
    @Schema(description = "库位ID")
    private Long locationId;

    /**
     * 库区类型：PLANE-平面 STEREO-立体 RACK-货架
     */
    @Schema(description = "库区类型：PLANE-平面 STEREO-立体 RACK-货架")
    private String storageType;

    /**
     * 容量
     */
    @Schema(description = "容量")
    private Integer capacity;

    /**
     * 是否启用：0-禁用 1-启用
     */
    @NotNull(message = "是否启用不能为空")
    @Schema(description = "是否启用")
    private Integer isEnabled;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500")
    @Schema(description = "备注")
    private String remark;
}
