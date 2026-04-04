package com.abtk.product.service.permission.filter;

import com.abtk.product.common.permission.enums.DataScopeType;
import com.abtk.product.common.utils.StringUtils;
import com.abtk.product.dao.entity.SysRole;
import com.abtk.product.dao.entity.SysUser;
import com.abtk.product.service.permission.entity.SysDataPermissionField;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 部门数据权限过滤器
 * 
 * @author backend2
 */
@Component
public class DeptPermissionFilter implements DataPermissionFilter {
    
    @Override
    public String getFilterType() {
        return "DEPT";
    }
    
    @Override
    public int getPriority() {
        return 10;
    }
    
    @Override
    public boolean needFilter(SysUser user, String permission) {
        if (user == null || user.getRoles() == null) {
            return false;
        }
        
        // 超级管理员不过滤
        if (user.isAdmin()) {
            return false;
        }
        
        // 检查是否有部门相关的数据权限
        for (SysRole role : user.getRoles()) {
            String dataScope = role.getDataScope();
            if (DataScopeType.containsDeptScope(dataScope)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String buildCondition(SysUser user, SysDataPermissionField config, String alias, String permission) {
        if (!needFilter(user, permission)) {
            return "";
        }
        
        StringBuilder sql = new StringBuilder();
        String fieldName = getFieldName(config, alias);
        List<String> conditions = new ArrayList<>();
        
        // 收集所有角色产生的条件
        Set<Long> customRoleIds = new HashSet<>();
        
        for (SysRole role : user.getRoles()) {
            if (!StringUtils.equals(role.getStatus(), "0")) {
                continue;
            }
            
            String dataScope = role.getDataScope();
            
            // 全部数据权限
            if (DataScopeType.ALL.getCode().equals(dataScope)) {
                return ""; // 不过滤
            }
            
            // 自定义数据权限
            if (DataScopeType.CUSTOM.getCode().equals(dataScope)) {
                if (hasPermission(role, permission)) {
                    customRoleIds.add(role.getRoleId());
                }
            }
            // 本部门数据权限
            else if (DataScopeType.DEPT.getCode().equals(dataScope)) {
                if (hasPermission(role, permission)) {
                    conditions.add(buildDeptCondition(fieldName, user.getDeptId()));
                }
            }
            // 本部门及以下数据权限
            else if (DataScopeType.DEPT_AND_CHILD.getCode().equals(dataScope)) {
                if (hasPermission(role, permission)) {
                    conditions.add(buildDeptAndChildCondition(fieldName, user.getDeptId()));
                }
            }
        }
        
        // 处理自定义数据权限（多个角色合并为IN查询）
        if (!customRoleIds.isEmpty()) {
            conditions.add(buildCustomCondition(fieldName, customRoleIds));
        }
        
        // 如果没有条件，返回不查询任何数据
        if (conditions.isEmpty()) {
            return fieldName + " = 0";
        }
        
        // 合并条件（OR连接）
        if (conditions.size() == 1) {
            return conditions.get(0);
        }
        return "(" + String.join(" OR ", conditions) + ")";
    }
    
    /**
     * 检查角色是否有指定权限
     */
    private boolean hasPermission(SysRole role, String permission) {
        if (StringUtils.isEmpty(permission) || role.getPermissions() == null) {
            return true;
        }
        String[] perms = permission.split(",");
        for (String perm : perms) {
            if (role.getPermissions().contains(perm.trim())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 获取字段名
     */
    private String getFieldName(SysDataPermissionField config, String alias) {
        String field = (config != null && StringUtils.isNotEmpty(config.getDeptField())) 
                ? config.getDeptField() 
                : "dept_id";
        return StringUtils.isNotEmpty(alias) ? alias + "." + field : field;
    }
    
    /**
     * 构建本部门条件
     */
    private String buildDeptCondition(String fieldName, Long deptId) {
        if (deptId == null || !isNumeric(deptId.toString())) {
            return fieldName + " = 0";
        }
        return fieldName + " = " + deptId;
    }
    
    /**
     * 构建本部门及以下条件
     */
    private String buildDeptAndChildCondition(String fieldName, Long deptId) {
        if (deptId == null || !isNumeric(deptId.toString())) {
            return fieldName + " = 0";
        }
        // 使用find_in_set查询部门及以下
        return fieldName + " IN (SELECT dept_id FROM sys_dept WHERE dept_id = " + deptId + 
                " OR FIND_IN_SET(" + deptId + ", ancestors))";
    }
    
    /**
     * 构建自定义数据权限条件
     */
    private String buildCustomCondition(String fieldName, Set<Long> roleIds) {
        if (roleIds.isEmpty()) {
            return fieldName + " = 0";
        }
        
        // 校验所有roleId为纯数字
        boolean allNumeric = roleIds.stream().allMatch(id -> id != null && isNumeric(id.toString()));
        if (!allNumeric) {
            return fieldName + " = 0";
        }
        
        String ids = String.join(",", roleIds.stream().map(String::valueOf).toArray(String[]::new));
        return fieldName + " IN (SELECT dept_id FROM sys_role_dept WHERE role_id IN (" + ids + "))";
    }
    
    /**
     * 检查是否为数字
     */
    private boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }
}
