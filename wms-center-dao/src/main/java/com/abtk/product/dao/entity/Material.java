package com.abtk.product.dao.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Material implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 物料Id */
    private Long id;

    /** 物料编码 */
    private String materialCode;

    /** 物料名称 */
    private String materialName;

    /** 规格 */
    private String spec;

    /** 单位 */
    private String unit;

    /** 分类 */
    private String category;

    /** 状态：0禁用 1启用 */
    private Integer status;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;

    /** 删除标志：0存在 1删除 */
    private Integer isDeleted;

    /** 物料英文名称 */
    private String materialEnName;

    /** 出口名称 */
    private String exportName;

    /** 品牌 */
    private String brand;

    /** 货号 */
    private String itemNo;

    /** 包装规格 */
    private String packageSpec;

    /** 存储条件 */
    private String storageCondition;

    /** 运输条件 */
    private String transportCondition;

    /** 长(cm) */
    private BigDecimal length;

    /** 宽(cm) */
    private BigDecimal width;

    /** 高(cm) */
    private BigDecimal height;

    /** 物流包装 */
    private String logisticsPackage;

    /** 箱型号 */
    private String boxModel;

    /** 产品外包装 */
    private String outerPackage;

    /** 是否必检 */
    private Boolean requireQc;

    /** 供应商品牌 */
    private String supplierBrand;

    /** 供应商货号 */
    private String supplierItemNo;

    /** 供应商规格 */
    private String supplierSpec;

    /** ERP同步备注 */
    private String erpSyncRemark;

    /** 备注 */
    private String remarks;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMaterialCode() { return materialCode; }
    public void setMaterialCode(String materialCode) { this.materialCode = materialCode; }
    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }
    public String getSpec() { return spec; }
    public void setSpec(String spec) { this.spec = spec; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }
    public String getMaterialEnName() { return materialEnName; }
    public void setMaterialEnName(String materialEnName) { this.materialEnName = materialEnName; }
    public String getExportName() { return exportName; }
    public void setExportName(String exportName) { this.exportName = exportName; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getItemNo() { return itemNo; }
    public void setItemNo(String itemNo) { this.itemNo = itemNo; }
    public String getPackageSpec() { return packageSpec; }
    public void setPackageSpec(String packageSpec) { this.packageSpec = packageSpec; }
    public String getStorageCondition() { return storageCondition; }
    public void setStorageCondition(String storageCondition) { this.storageCondition = storageCondition; }
    public String getTransportCondition() { return transportCondition; }
    public void setTransportCondition(String transportCondition) { this.transportCondition = transportCondition; }
    public BigDecimal getLength() { return length; }
    public void setLength(BigDecimal length) { this.length = length; }
    public BigDecimal getWidth() { return width; }
    public void setWidth(BigDecimal width) { this.width = width; }
    public BigDecimal getHeight() { return height; }
    public void setHeight(BigDecimal height) { this.height = height; }
    public String getLogisticsPackage() { return logisticsPackage; }
    public void setLogisticsPackage(String logisticsPackage) { this.logisticsPackage = logisticsPackage; }
    public String getBoxModel() { return boxModel; }
    public void setBoxModel(String boxModel) { this.boxModel = boxModel; }
    public String getOuterPackage() { return outerPackage; }
    public void setOuterPackage(String outerPackage) { this.outerPackage = outerPackage; }
    public Boolean getRequireQc() { return requireQc; }
    public void setRequireQc(Boolean requireQc) { this.requireQc = requireQc; }
    public String getSupplierBrand() { return supplierBrand; }
    public void setSupplierBrand(String supplierBrand) { this.supplierBrand = supplierBrand; }
    public String getSupplierItemNo() { return supplierItemNo; }
    public void setSupplierItemNo(String supplierItemNo) { this.supplierItemNo = supplierItemNo; }
    public String getSupplierSpec() { return supplierSpec; }
    public void setSupplierSpec(String supplierSpec) { this.supplierSpec = supplierSpec; }
    public String getErpSyncRemark() { return erpSyncRemark; }
    public void setErpSyncRemark(String erpSyncRemark) { this.erpSyncRemark = erpSyncRemark; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
