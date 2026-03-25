package com.abclonal.product.api.domain.request.sys;

import com.abclonal.product.api.domain.request.BaseRequest;
import com.abclonal.product.common.annotation.Excel;
import com.abclonal.product.common.annotation.Excel.ColumnType;
import com.abclonal.product.common.annotation.Excel.Type;
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
          
    @Excel(name = "主键ID", type = Type.ALL, cellType = ColumnType.NUMERIC)
    @Schema(description = "主键ID")
    private Long id;
    
    /**
     * 枚举编码，如：container_type
     */
          
    @Excel(name = "枚举编码，如：container_type", type = Type.ALL)
    @Schema(description = "枚举编码，如：container_type")
    private String enumCode;
    
    /**
     * 枚举名称，如：容器类型
     */
          
    @Excel(name = "枚举名称，如：容器类型", type = Type.ALL)
    @Schema(description = "枚举名称，如：容器类型")
    private String enumName;
    
    /**
     * 枚举描述
     */
          
    @Excel(name = "枚举描述", type = Type.ALL)
    @Schema(description = "枚举描述")
    private String enumDesc;
    
    /**
     * 分类编码，如：LOCATION
     */
          
    @Excel(name = "分类编码，如：LOCATION", type = Type.ALL)
    @Schema(description = "分类编码，如：LOCATION")
    private String categoryCode;
    
    /**
     * 分类名称，如：库位相关
     */
          
    @Excel(name = "分类名称，如：库位相关", type = Type.ALL)
    @Schema(description = "分类名称，如：库位相关")
    private String categoryName;
    
    /**
     * 是否启用
     */
          
    @Excel(name = "是否启用", type = Type.ALL, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否启用")
    private Integer isEnabled;
    
    /**
     * 排序
     */
          
    @Excel(name = "排序", type = Type.ALL, cellType = ColumnType.NUMERIC)
    @Schema(description = "排序")
    private Integer sortOrder;
    

}
