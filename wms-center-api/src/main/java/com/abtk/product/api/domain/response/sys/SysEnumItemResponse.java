package com.abtk.product.api.domain.response.sys;

import com.abtk.product.api.domain.response.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


/**
 * 枚举明细表(SysEnumItem)出参
 *
 * @author lht
 * @since 2026-03-06 16:08:20
 */
@Data
@Schema(description = "SysEnumItem请求出参")
public class SysEnumItemResponse extends BaseResponse {
    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Long id;
    
    /**
     * 所属枚举编码
     */
    @Schema(description = "所属枚举编码")
    private String enumCode;
    
    /**
     * 枚举项键值，如：BOX
     */
    @Schema(description = "枚举项键值，如：BOX")
    private String itemKey;
    
    /**
     * 枚举项显示值，如：箱
     */
    @Schema(description = "枚举项显示值，如：箱")
    private String itemValue;
    
    /**
     * 枚举项描述
     */
    @Schema(description = "枚举项描述")
    private String itemDesc;
    
    /**
     * 排序顺序
     */
    @Schema(description = "排序顺序")
    private Integer sortOrder;
    
    /**
     * 是否默认值
     */
    @Schema(description = "是否默认值")
    private Integer isDefault;
    
    /**
     * 是否启用
     */
    @Schema(description = "是否启用")
    private Integer isEnabled;
    

}

