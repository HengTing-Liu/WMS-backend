package com.abclonal.product.service.permission.filter;

import com.abclonal.product.common.permission.enums.DataScopeType;
import com.abclonal.product.common.utils.StringUtils;
import com.abclonal.product.dao.entity.SysRole;
import com.abclonal.product.dao.entity.SysUser;
import com.abclonal.product.service.permission.entity.SysDataPermissionField;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 仓库数据权限过滤器
 * 
 * @author backend2
 */
@Component
public class WarehousePermissionFilter implements DataPermissionFilter {
    
    @Override
    public String getFilterType() {
        return "WAREHOUSE";
    }
    
    @Override
    public int getPriority() {
        return 20;
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
        
        // 检查是否有仓库相关的数据权限
        for (SysRole role : user.getRoles()) {
            if (DataScopeType.WAREHOUSE.getCode().equals(role.getDataScope())) {
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
        
        String fieldName = getFieldName(config, alias);
        
        // 收集用户所有角色可访问的仓库编码
        Set<String> warehouseCodes = new HashSet<>();
        
        for (SysRole role : user.getRoles()) {
            if (!StringUtils.equals(role.getStatus(), "0")) {
                continue;
            }
            
            String dataScope = role.getDataScope();
            
            // 全部数据权限
            if (DataScopeType.ALL.getCode().equals(dataScope)) {
                return ""; // 不过滤
            }
            
            // 指定仓库权限
            if (DataScopeType.WAREHOUSE.getCode().equals(dataScope)) {
                if (hasPermission(role, permission)) {
                    // 从角色-仓库关联表获取仓库编码
                    // 这里假设role对象中有warehouseCodes属性或通过其他方式获取
                    Set<String> codes = getRoleWarehouseCodes(role.getRoleId());
                    if (codes != null) {
                        warehouseCodes.addAll(codes);
                    }
                }
            }
        }
        
        // 如果没有仓库权限，返回不查询任何数据的条件
        if (warehouseCodes.isEmpty()) {
            return fieldName + " = '__NO_ACCESS__'";
        }
        
        // 构建IN条件
        return buildInCondition(fieldName, warehouseCodes);
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
     * 获取角色的仓库编码列表
     * 实际项目中需要从数据库或缓存查询
     */
    private Set<String> getRoleWarehouseCodes(Long roleId) {
        // TODO: 从sys_role_warehouse表查询
        // 这里返回空集合，实际使用时需要实现查询逻辑
        return new HashSet<>();
    }
    
    /**
     * 获取字段名
     */
    private String getFieldName(SysDataPermissionField config, String alias) {
        String field = (config != null && StringUtils.isNotEmpty(config.getWarehouseField())) 
                ? config.getWarehouseField() 
                : "warehouse_code";
        return StringUtils.isNotEmpty(alias) ? alias + "." + field : field;
    }
    
    /**
     * 构建IN条件
     */
    private String buildInCondition(String fieldName, Set<String> codes) {
        if (codes == null || codes.isEmpty()) {
            return fieldName + " = '__NO_ACCESS__'";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(fieldName).append(" IN (");
        
        boolean first = true;
        for (String code : codes) {
            // 防止SQL注入，只允许字母数字下划线
            if (!isValidWarehouseCode(code)) {
                continue;
            }
            if (!first) {
                sb.append(", ");
            }
            sb.append("'").append(code).append("'");
            first = false;
        }
        
        sb.append(")");
        return sb.toString();
    }
    
    /**
     * 校验仓库编码格式
     */
    private boolean isValidWarehouseCode(String code) {
        return code != null && code.matches("^[a-zA-Z0-9_-]+$");
    }
}
