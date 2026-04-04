package com.abtk.product.service.permission.entity;

import com.abtk.product.common.annotation.Excel;
import com.abtk.product.dao.entity.BaseEntity;

/**
 * 数据权限字段配置表
 * 用于配置每张表的数据权限控制字段
 * 
 * @author backend2
 */
public class SysDataPermissionField extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /** 配置ID */
    private Long id;
    
    /** 表编码（关联sys_table_meta.table_code） */
    @Excel(name = "表编码")
    private String tableCode;
    
    /** 表名称 */
    @Excel(name = "表名称")
    private String tableName;
    
    /** 部门字段名（用于部门权限过滤） */
    @Excel(name = "部门字段")
    private String deptField;
    
    /** 仓库字段名（用于仓库权限过滤） */
    @Excel(name = "仓库字段")
    private String warehouseField;
    
    /** 用户字段名（用于本人权限过滤） */
    @Excel(name = "用户字段")
    private String userField;
    
    /** 公司字段名（用于公司权限过滤） */
    @Excel(name = "公司字段")
    private String companyField;
    
    /** 是否启用部门权限控制（0-否 1-是） */
    @Excel(name = "启用部门权限", readConverterExp = "0=否,1=是")
    private Integer enableDept;
    
    /** 是否启用仓库权限控制（0-否 1-是） */
    @Excel(name = "启用仓库权限", readConverterExp = "0=否,1=是")
    private Integer enableWarehouse;
    
    /** 是否启用本人权限（0-否 1-是） */
    @Excel(name = "启用本人权限", readConverterExp = "0=否,1=是")
    private Integer enableSelf;
    
    /** 是否启用公司权限（0-否 1-是） */
    @Excel(name = "启用公司权限", readConverterExp = "0=否,1=是")
    private Integer enableCompany;
    
    /** 状态（0-正常 1-停用） */
    private Integer status;
    
    /** 备注 */
    private String remark;
    
    // ==================== 构造方法 ====================
    
    public SysDataPermissionField() {
    }
    
    public SysDataPermissionField(String tableCode) {
        this.tableCode = tableCode;
    }
    
    // ==================== Getter/Setter ====================
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTableCode() {
        return tableCode;
    }
    
    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }
    
    public String getTableName() {
        return tableName;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public String getDeptField() {
        return deptField;
    }
    
    public void setDeptField(String deptField) {
        this.deptField = deptField;
    }
    
    public String getWarehouseField() {
        return warehouseField;
    }
    
    public void setWarehouseField(String warehouseField) {
        this.warehouseField = warehouseField;
    }
    
    public String getUserField() {
        return userField;
    }
    
    public void setUserField(String userField) {
        this.userField = userField;
    }
    
    public String getCompanyField() {
        return companyField;
    }
    
    public void setCompanyField(String companyField) {
        this.companyField = companyField;
    }
    
    public Integer getEnableDept() {
        return enableDept;
    }
    
    public void setEnableDept(Integer enableDept) {
        this.enableDept = enableDept;
    }
    
    public Integer getEnableWarehouse() {
        return enableWarehouse;
    }
    
    public void setEnableWarehouse(Integer enableWarehouse) {
        this.enableWarehouse = enableWarehouse;
    }
    
    public Integer getEnableSelf() {
        return enableSelf;
    }
    
    public void setEnableSelf(Integer enableSelf) {
        this.enableSelf = enableSelf;
    }
    
    public Integer getEnableCompany() {
        return enableCompany;
    }
    
    public void setEnableCompany(Integer enableCompany) {
        this.enableCompany = enableCompany;
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
    
    // ==================== 业务方法 ====================
    
    /**
     * 检查是否启用部门权限
     */
    public boolean isDeptEnabled() {
        return enableDept != null && enableDept == 1 && deptField != null && !deptField.isEmpty();
    }
    
    /**
     * 检查是否启用仓库权限
     */
    public boolean isWarehouseEnabled() {
        return enableWarehouse != null && enableWarehouse == 1 && warehouseField != null && !warehouseField.isEmpty();
    }
    
    /**
     * 检查是否启用本人权限
     */
    public boolean isSelfEnabled() {
        return enableSelf != null && enableSelf == 1 && userField != null && !userField.isEmpty();
    }
    
    /**
     * 检查是否启用公司权限
     */
    public boolean isCompanyEnabled() {
        return enableCompany != null && enableCompany == 1 && companyField != null && !companyField.isEmpty();
    }
    
    @Override
    public String toString() {
        return "SysDataPermissionField{" +
                "id=" + id +
                ", tableCode='" + tableCode + '\'' +
                ", tableName='" + tableName + '\'' +
                ", deptField='" + deptField + '\'' +
                ", warehouseField='" + warehouseField + '\'' +
                ", userField='" + userField + '\'' +
                ", companyField='" + companyField + '\'' +
                ", enableDept=" + enableDept +
                ", enableWarehouse=" + enableWarehouse +
                ", enableSelf=" + enableSelf +
                ", enableCompany=" + enableCompany +
                ", status=" + status +
                '}';
    }
}
