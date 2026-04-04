package com.abtk.product.dao.entity;

import com.abtk.product.common.annotation.Excel;
import com.abtk.product.common.annotation.Excel.ColumnType;
import com.abtk.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 枚举定义表(SysEnumDefine)实体类
 *
 * @author lht
 * @since 2026-03-06 13:30:46
 */
public class SysEnumDefine extends BaseEntity {
    private static final long serialVersionUID = -78157467775693127L;
    
    /**
     * 主键ID
     */
       
    @Excel(name = "主键ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "主键ID")
    private Long id;
    
    /**
     * 枚举编码，如：container_type
     */
       
    @Excel(name = "枚举编码，如：container_type", type = Type.EXPORT)
    @Schema(description = "枚举编码，如：container_type")
    private String enumCode;
    
    /**
     * 枚举名称，如：容器类型
     */
       
    @Excel(name = "枚举名称，如：容器类型", type = Type.EXPORT)
    @Schema(description = "枚举名称，如：容器类型")
    private String enumName;
    
    /**
     * 枚举描述
     */
       
    @Excel(name = "枚举描述", type = Type.EXPORT)
    @Schema(description = "枚举描述")
    private String enumDesc;
    
    /**
     * 分类编码，如：LOCATION
     */
       
    @Excel(name = "分类编码，如：LOCATION", type = Type.EXPORT)
    @Schema(description = "分类编码，如：LOCATION")
    private String categoryCode;
    
    /**
     * 分类名称，如：库位相关
     */
       
    @Excel(name = "分类名称，如：库位相关", type = Type.EXPORT)
    @Schema(description = "分类名称，如：库位相关")
    private String categoryName;
    
    /**
     * 是否启用
     */
       
    @Excel(name = "是否启用", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否启用")
    private Integer isEnabled;
    
    /**
     * 排序
     */
       
    @Excel(name = "排序", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "排序")
    private Integer sortOrder;
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEnumCode() {
        return enumCode;
    }

    public void setEnumCode(String enumCode) {
        this.enumCode = enumCode;
    }
    
    public String getEnumName() {
        return enumName;
    }

    public void setEnumName(String enumName) {
        this.enumName = enumName;
    }
    
    public String getEnumDesc() {
        return enumDesc;
    }

    public void setEnumDesc(String enumDesc) {
        this.enumDesc = enumDesc;
    }
    
    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }
    
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }
    
    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
    
}

