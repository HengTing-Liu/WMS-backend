package com.abtk.product.api.domain.request.sys;

import com.abtk.product.api.domain.request.BaseRequest;
import com.abtk.product.common.annotation.Excel;
import com.abtk.product.common.annotation.Excel.ColumnType;
import com.abtk.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 枚举明细表(SysEnumItem)入参
 *
 * @author lht
 * @since 2026-03-06 16:07:42
 */
@Data
@Schema(description = "SysEnumItem请求入参")
public class SysEnumItemRequest extends BaseRequest {
    /**
     * 主键ID
     */
          
    @Excel(name = "主键ID", type = Type.ALL, cellType = ColumnType.NUMERIC)
    @Schema(description = "主键ID")
    private Long id;
    
    /**
     * 所属枚举编码
     */
          
    @Excel(name = "所属枚举编码", type = Type.ALL)
    @Schema(description = "所属枚举编码")
    private String enumCode;
    
    /**
     * 枚举项键值，如：BOX
     */
          
    @Excel(name = "枚举项键值，如：BOX", type = Type.ALL)
    @Schema(description = "枚举项键值，如：BOX")
    private String itemKey;
    
    /**
     * 枚举项显示值，如：箱
     */
          
    @Excel(name = "枚举项显示值，如：箱", type = Type.ALL)
    @Schema(description = "枚举项显示值，如：箱")
    private String itemValue;
    
    /**
     * 枚举项描述
     */
          
    @Excel(name = "枚举项描述", type = Type.ALL)
    @Schema(description = "枚举项描述")
    private String itemDesc;
    
    /**
     * 排序顺序
     */
          
    @Excel(name = "排序顺序", type = Type.ALL, cellType = ColumnType.NUMERIC)
    @Schema(description = "排序顺序")
    private Integer sortOrder;
    
    /**
     * 是否默认值
     */
          
    @Excel(name = "是否默认值", type = Type.ALL, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否默认值")
    private Integer isDefault;
    
    /**
     * 是否启用
     */
          
    @Excel(name = "是否启用", type = Type.ALL, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否启用")
    private Integer isEnabled;
    

}
