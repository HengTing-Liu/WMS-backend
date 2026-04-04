package com.abtk.product.dao.entity;

import com.abtk.product.common.annotation.Excel;
import com.abtk.product.common.annotation.Excel.ColumnType;
import com.abtk.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 枚举明细表(SysEnumItem)实体类
 *
 * @author lht
 * @since 2026-03-06 16:06:56
 */
public class SysEnumItem extends BaseEntity {
    private static final long serialVersionUID = -96147545116464703L;
    
    /**
     * 主键ID
     */
       
    @Excel(name = "主键ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "主键ID")
    private Long id;
    
    /**
     * 所属枚举编码
     */
       
    @Excel(name = "所属枚举编码", type = Type.EXPORT)
    @Schema(description = "所属枚举编码")
    private String enumCode;
    
    /**
     * 枚举项键值，如：BOX
     */
       
    @Excel(name = "枚举项键值，如：BOX", type = Type.EXPORT)
    @Schema(description = "枚举项键值，如：BOX")
    private String itemKey;
    
    /**
     * 枚举项显示值，如：箱
     */
       
    @Excel(name = "枚举项显示值，如：箱", type = Type.EXPORT)
    @Schema(description = "枚举项显示值，如：箱")
    private String itemValue;
    
    /**
     * 枚举项描述
     */
       
    @Excel(name = "枚举项描述", type = Type.EXPORT)
    @Schema(description = "枚举项描述")
    private String itemDesc;
    
    /**
     * 排序顺序
     */
       
    @Excel(name = "排序顺序", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "排序顺序")
    private Integer sortOrder;
    
    /**
     * 是否默认值
     */
       
    @Excel(name = "是否默认值", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否默认值")
    private Integer isDefault;
    
    /**
     * 是否启用
     */
       
    @Excel(name = "是否启用", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否启用")
    private Integer isEnabled;
    
    

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
    
    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }
    
    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }
    
    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }
    
    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }
    
    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }
    
}

