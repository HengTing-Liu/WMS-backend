package com.abtk.product.service.permission.entity;

import com.abtk.product.common.annotation.Excel;
import com.abtk.product.dao.entity.BaseEntity;

/**
 * 角色仓库权限关联表
 * 用于配置角色可以访问的仓库
 * 
 * @author backend2
 */
public class SysRoleWarehouse extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /** 关联ID */
    private Long id;
    
    /** 角色ID */
    @Excel(name = "角色ID")
    private Long roleId;
    
    /** 仓库编码 */
    @Excel(name = "仓库编码")
    private String warehouseCode;
    
    /** 仓库名称 */
    @Excel(name = "仓库名称")
    private String warehouseName;
    
    /** 状态（0-正常 1-停用） */
    private Integer status;
    
    /** 备注 */
    private String remark;
    
    // ==================== 构造方法 ====================
    
    public SysRoleWarehouse() {
    }
    
    public SysRoleWarehouse(Long roleId, String warehouseCode) {
        this.roleId = roleId;
        this.warehouseCode = warehouseCode;
    }
    
    // ==================== Getter/Setter ====================
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getRoleId() {
        return roleId;
    }
    
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    @Override
    public String toString() {
        return "SysRoleWarehouse{" +
                "id=" + id +
                ", roleId=" + roleId +
                ", warehouseCode='" + warehouseCode + '\'' +
                ", warehouseName='" + warehouseName + '\'' +
                ", status=" + status +
                '}';
    }
}
