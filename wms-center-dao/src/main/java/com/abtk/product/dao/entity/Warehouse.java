package com.abtk.product.dao.entity;

import java.io.Serializable;

/**
 * 仓库实体类
 * 表名: sys_warehouse
 */
public class Warehouse extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 仓库编码 */
    private String warehouseCode;

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

    /** 所属公司 */
    private String company;

    /** 是否启用：0-禁用 1-启用 */
    private Integer isEnabled;

    /** 逻辑删除：0-未删除 1-已删除 */
    private Integer isdeleted;

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Integer getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(Integer isdeleted) {
        this.isdeleted = isdeleted;
    }
}
