package com.abclonal.product.dao.gengrator.entity.permission;

import com.abclonal.product.dao.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 角色数据权限对象 role_data_permission
 *
 * @author backend1
 */
@Schema(description = "角色数据权限")
public class RoleDataPermission extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 角色ID

     */
    @Schema(description = "角色ID", required = true)
    private Long roleId;

    /**
     * 表编码
     */
    @Schema(description = "表编码", required = true)
    private String tableCode;

    /**
     * 数据权限范围：ALL/CUSTOM/DEPT/DEPT_AND_CHILD/SELF
     */
    @Schema(description = "数据权限范围", allowableValues = {"ALL", "CUSTOM", "DEPT", "DEPT_AND_CHILD", "SELF"}, defaultValue = "DEPT")
    private String dataScope;

    /**
     * 部门白名单（JSON数组）
     */
    @Schema(description = "部门白名单JSON")
    private String deptWhitelist;

    /**
     * 部门黑名单（JSON数组）
     */
    @Schema(description = "部门黑名单JSON")
    private String deptBlacklist;

    /**
     * 用户白名单（JSON数组）
     */
    @Schema(description = "用户白名单JSON")
    private String userWhitelist;

    /**
     * 用户黑名单（JSON数组）
     */
    @Schema(description = "用户黑名单JSON")
    private String userBlacklist;

    /**
     * 自定义SQL条件
     */
    @Schema(description = "自定义SQL条件")
    private String customSql;

    /**
     * 状态：0=禁用，1=启用
     */
    @Schema(description = "状态", allowableValues = {"0", "1"}, defaultValue = "1")
    private Integer status;

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

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    public String getDeptWhitelist() {
        return deptWhitelist;
    }

    public void setDeptWhitelist(String deptWhitelist) {
        this.deptWhitelist = deptWhitelist;
    }

    public String getDeptBlacklist() {
        return deptBlacklist;
    }

    public void setDeptBlacklist(String deptBlacklist) {
        this.deptBlacklist = deptBlacklist;
    }

    public String getUserWhitelist() {
        return userWhitelist;
    }

    public void setUserWhitelist(String userWhitelist) {
        this.userWhitelist = userWhitelist;
    }

    public String getUserBlacklist() {
        return userBlacklist;
    }

    public void setUserBlacklist(String userBlacklist) {
        this.userBlacklist = userBlacklist;
    }

    public String getCustomSql() {
        return customSql;
    }

    public void setCustomSql(String customSql) {
        this.customSql = customSql;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
                .append("roleId", getRoleId())
                .append("tableCode", getTableCode())
                .append("dataScope", getDataScope())
                .append("deptWhitelist", getDeptWhitelist())
                .append("deptBlacklist", getDeptBlacklist())
                .append("userWhitelist", getUserWhitelist())
                .append("userBlacklist", getUserBlacklist())
                .append("customSql", getCustomSql())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("deleted", getDeleted())
                .append("remark", getRemark())
                .toString();
    }
}
