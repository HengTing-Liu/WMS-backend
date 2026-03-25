package com.abclonal.product.dao.gengrator.entity.permission;

import com.abclonal.product.dao.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 权限规则日志对象 permission_rule_log
 *
 * @author backend1
 */
@Schema(description = "权限规则日志")
public class PermissionRuleLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 操作类型：SAVE/UPDATE/DELETE/QUERY
     */
    @Schema(description = "操作类型", allowableValues = {"SAVE", "UPDATE", "DELETE", "QUERY"})
    private String operationType;

    /**
     * 权限维度：USER/DEPT/ROLE/COMPANY
     */
    @Schema(description = "权限维度", allowableValues = {"USER", "DEPT", "ROLE", "COMPANY"})
    private String permissionDimension;

    /**
     * 维度ID（用户ID/部门ID/角色ID/公司ID）
     */
    @Schema(description = "维度ID")
    private Long dimensionId;

    /**
     * 表编码
     */
    @Schema(description = "表编码")
    private String tableCode;

    /**
     * 旧值（JSON格式）
     */
    @Schema(description = "旧值JSON")
    private String oldValue;

    /**
     * 新值（JSON格式）
     */
    @Schema(description = "新值JSON")
    private String newValue;

    /**
     * 操作结果：SUCCESS/FAILURE
     */
    @Schema(description = "操作结果", allowableValues = {"SUCCESS", "FAILURE"})
    private String operationResult;

    /**
     * 错误信息
     */
    @Schema(description = "错误信息")
    private String errorMessage;

    /**
     * 操作IP
     */
    @Schema(description = "操作IP")
    private String operationIp;

    /**
     * 浏览器信息
     */
    @Schema(description = "浏览器信息")
    private String browserInfo;

    /**
     * 逻辑删除：0=未删除，1=已删除
     */
    @Schema(description = "逻辑删除")
    private Integer deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getPermissionDimension() {
        return permissionDimension;
    }

    public void setPermissionDimension(String permissionDimension) {
        this.permissionDimension = permissionDimension;
    }

    public Long getDimensionId() {
        return dimensionId;
    }

    public void setDimensionId(Long dimensionId) {
        this.dimensionId = dimensionId;
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(String operationResult) {
        this.operationResult = operationResult;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getOperationIp() {
        return operationIp;
    }

    public void setOperationIp(String operationIp) {
        this.operationIp = operationIp;
    }

    public String getBrowserInfo() {
        return browserInfo;
    }

    public void setBrowserInfo(String browserInfo) {
        this.browserInfo = browserInfo;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("operationType", getOperationType())
                .append("permissionDimension", getPermissionDimension())
                .append("dimensionId", getDimensionId())
                .append("tableCode", getTableCode())
                .append("oldValue", getOldValue())
                .append("newValue", getNewValue())
                .append("operationResult", getOperationResult())
                .append("errorMessage", getErrorMessage())
                .append("operationIp", getOperationIp())
                .append("browserInfo", getBrowserInfo())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("deleted", getDeleted())
                .append("remark", getRemark())
                .toString();
    }
}
