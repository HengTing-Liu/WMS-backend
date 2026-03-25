package com.abclonal.product.service.permission.filter;

import com.abclonal.product.common.permission.enums.DataScopeType;
import com.abclonal.product.common.utils.StringUtils;
import com.abclonal.product.dao.entity.SysRole;
import com.abclonal.product.dao.entity.SysUser;
import com.abclonal.product.service.permission.entity.SysDataPermissionField;
import org.springframework.stereotype.Component;

/**
 * 本人数据权限过滤器
 * 仅允许查看本人创建的数据
 * 
 * @author backend2
 */
@Component
public class SelfPermissionFilter implements DataPermissionFilter {
    
    @Override
    public String getFilterType() {
        return "SELF";
    }
    
    @Override
    public int getPriority() {
        return 30;
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
        
        // 检查是否有本人数据权限
        for (SysRole role : user.getRoles()) {
            if (DataScopeType.SELF.getCode().equals(role.getDataScope())) {
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
        Long userId = user.getUserId();
        
        if (userId == null || !isNumeric(userId.toString())) {
            return fieldName + " = 0";
        }
        
        return fieldName + " = " + userId;
    }
    
    /**
     * 获取字段名
     */
    private String getFieldName(SysDataPermissionField config, String alias) {
        String field = (config != null && StringUtils.isNotEmpty(config.getUserField())) 
                ? config.getUserField() 
                : "create_by";
        return StringUtils.isNotEmpty(alias) ? alias + "." + field : field;
    }
    
    /**
     * 检查是否为数字
     */
    private boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }
}
