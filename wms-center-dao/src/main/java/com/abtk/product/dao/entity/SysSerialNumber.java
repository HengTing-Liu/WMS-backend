package com.abtk.product.dao.entity;

import com.abtk.product.common.annotation.Excel;
import com.abtk.product.common.annotation.Excel.ColumnType;
import com.abtk.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;



/**
 * 流水号规则表(SysSerialNumber)实体类
 *
 * @author lht
 * @since 2026-03-09 13:30:00
 */
public class SysSerialNumber extends BaseEntity {
    private static final long serialVersionUID = -78157467775693128L;

    /**
     * 主键ID
     */
    @Excel(name = "主键ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 名称
     */
    @Excel(name = "名称", type = Type.EXPORT)
    @Schema(description = "名称")
    private String name;

    /**
     * 前缀
     */
    @Excel(name = "前缀", type = Type.EXPORT)
    @Schema(description = "前缀")
    private String prefix;

    /**
     * 数字规则：0-无，1-年，2-年月，3-年月日
     */
    @Excel(name = "数字规则", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "数字规则：0-无，1-年，2-年月，3-年月日")
    private Integer numberType;

    /**
     * 位数（序号长度，如3表示001-999）
     */
    @Excel(name = "位数", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "位数")
    private Integer digitLength;

    /**
     * 后缀
     */
    @Excel(name = "后缀", type = Type.EXPORT)
    @Schema(description = "后缀")
    private String suffix;

    /**
     * 起始值（序号从该值开始，默认1）
     */
    @Excel(name = "起始值", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "起始值")
    private Long startValue;

    /**
     * 重置规则：0-无，1-年，2-年月，3-年月日
     * 当重置周期变化时，序号重新从start_value开始
     */
    @Excel(name = "重置规则", type = Type.EXPORT)
    @Schema(description = "重置规则：0-无，1-年，2-年月，3-年月日")
    private String resetRule;

    /**
     * 当前值（下一个要使用的序号）
     */
    @Excel(name = "当前值", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "当前值")
    private Long currentValue;

    /**
     * 上次重置周期标识（用于判断是否重置序号）
     * 格式根据reset_rule：年-yyyy，年月-yyyyMM，年月日-yyyyMMdd
     */
    @Schema(description = "上次重置周期标识")
    private String lastResetKey;

    /**
     * 是否启用：0-禁用，1-启用
     */
    @Excel(name = "是否启用", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否启用：0-禁用，1-启用")
    private Integer isEnabled;

    /**
     * 应用表单字段（指定该流水号规则应用在哪个表单字段）
     */
    @Excel(name = "应用表单字段", type = Type.EXPORT)
    @Schema(description = "应用表单字段")
    private String applyFormField;

    /**
     * 使用范围
     */
    @Excel(name = "使用范围", type = Type.EXPORT)
    @Schema(description = "使用范围")
    private String usageScope;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getNumberType() {
        return numberType;
    }

    public void setNumberType(Integer numberType) {
        this.numberType = numberType;
    }

    public Integer getDigitLength() {
        return digitLength;
    }

    public void setDigitLength(Integer digitLength) {
        this.digitLength = digitLength;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Long getStartValue() {
        return startValue;
    }

    public void setStartValue(Long startValue) {
        this.startValue = startValue;
    }

    public String getResetRule() {
        return resetRule;
    }

    public void setResetRule(String resetRule) {
        this.resetRule = resetRule;
    }

    public Long getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Long currentValue) {
        this.currentValue = currentValue;
    }

    public String getLastResetKey() {
        return lastResetKey;
    }

    public void setLastResetKey(String lastResetKey) {
        this.lastResetKey = lastResetKey;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getApplyFormField() {
        return applyFormField;
    }

    public void setApplyFormField(String applyFormField) {
        this.applyFormField = applyFormField;
    }

    public String getUsageScope() {
        return usageScope;
    }

    public void setUsageScope(String usageScope) {
        this.usageScope = usageScope;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }
}
