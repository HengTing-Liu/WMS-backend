package com.abtk.product.service.permission.entity;

import com.abtk.product.common.annotation.Excel;
import com.abtk.product.dao.entity.BaseEntity;

/**
 * 用户仓库权限关联表
 * 用于配置用户可以访问的仓库
 * 
 * @author backend2
 */
public class SysUserWarehouse extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /** 关联ID */
    private Long id;
    
    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;
    
    /** 仓库编码 */
    @Excel(name = "仓库编码")
    private String warehouseCode;
    
    /** 仓库名称 */
    @Excel(name = "仓库名称")
    private String warehouseName;
    
    /** 状态（0-正常 1-停用） */
    private Integer status;
    
    /** 备注 */
    private String remarks;
    
    // ==================== 构造方法 ====================
    
    public SysUserWarehouse() {
    }
    
    public SysUserWarehouse(Long userId, String warehouseCode) {
        this.userId = userId;
        this.warehouseCode = warehouseCode;
    }
    
    // ==================== Getter/Setter ====================
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getWarehouseCode() {
        return warehouseCode;
    }
    
    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }
    
    public String getWarehouseName() {
        return warehouseName;
    }
    
    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    @Override
    public String toString() {
        return "SysUserWarehouse{" +
                "id=" + id +
                ", userId=" + userId +
                ", warehouseCode='" + warehouseCode + '\'' +
                ", warehouseName='" + warehouseName + '\'' +
                ", status=" + status +
                '}';
    }
}
