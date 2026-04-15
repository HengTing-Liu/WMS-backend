package com.abtk.product.dao.entity;

import java.io.Serializable;

/**
 * 仓库实体类
 * 表名: inv_warehouse
 */
public class Warehouse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 仓库类型 */
    private String warehouseType;

    /** 仓库编码 */
    private String warehouseCode;

    /** 所在地 */
    private String warehouseLocation;

    /** 仓库名称 */
    private String warehouseName;

    /** 温度分区 */
    private String temperatureZone;

    /** 质量分区 */
    private String qualityZone;

    /** 责任人工号 */
    private String employeeCode;

    /** 责任人 */
    private String employeeName;

    /** 责任部门编号 */
    private String deptCode;

    /** 责任部门全路径 */
    private String deptNameFullPath;

    /** ERP公司编码 */
    private String erpCompanyCode;

    /** ERP公司名称 */
    private String erpCompanyName;

    /** ERP仓库编码 */
    private String erpWarehouseCode;

    /** ERP货位编码 */
    private String erpLocationCode;

    /** 是否启用：0-禁用 1-启用 */
    private Integer isEnabled;

    /** 备注（对应数据库 remarks 字段） */
    private String remarks;

    /** 逻辑删除：0-未删除 1-已删除 */
    private Integer isDeleted;

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWarehouseType() {
        return warehouseType;
    }

    public void setWarehouseType(String warehouseType) {
        this.warehouseType = warehouseType;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    public void setWarehouseLocation(String warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getTemperatureZone() {
        return temperatureZone;
    }

    public void setTemperatureZone(String temperatureZone) {
        this.temperatureZone = temperatureZone;
    }

    public String getQualityZone() {
        return qualityZone;
    }

    public void setQualityZone(String qualityZone) {
        this.qualityZone = qualityZone;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptNameFullPath() {
        return deptNameFullPath;
    }

    public void setDeptNameFullPath(String deptNameFullPath) {
        this.deptNameFullPath = deptNameFullPath;
    }

    public String getErpCompanyCode() {
        return erpCompanyCode;
    }

    public void setErpCompanyCode(String erpCompanyCode) {
        this.erpCompanyCode = erpCompanyCode;
    }

    public String getErpCompanyName() {
        return erpCompanyName;
    }

    public void setErpCompanyName(String erpCompanyName) {
        this.erpCompanyName = erpCompanyName;
    }

    public String getErpWarehouseCode() {
        return erpWarehouseCode;
    }

    public void setErpWarehouseCode(String erpWarehouseCode) {
        this.erpWarehouseCode = erpWarehouseCode;
    }

    public String getErpLocationCode() {
        return erpLocationCode;
    }

    public void setErpLocationCode(String erpLocationCode) {
        this.erpLocationCode = erpLocationCode;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
