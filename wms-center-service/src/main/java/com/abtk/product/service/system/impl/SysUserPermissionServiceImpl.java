package com.abtk.product.service.system.impl;

import com.abtk.product.dao.entity.SysRole;
import com.abtk.product.dao.entity.SysUser;
import com.abtk.product.dao.mapper.SysMenuMapper;
import com.abtk.product.service.system.ISysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户权限服务实现类
 * 
 * @author backend
 * @since 2026-03-26
 */
@Service
public class SysUserPermissionServiceImpl implements ISysPermissionService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    /**
     * 获取角色数据权限
     * 
     * @param user 用户对象
     * @return 角色权限信息
     */
    @Override
    public Set<String> getRolePermission(SysUser user) {
        Set<String> rolePerms = new HashSet<String>();
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            for (SysRole role : user.getRoles()) {
                rolePerms.add(role.getRoleKey());
            }
        }
        return rolePerms;
    }

    /**
     * 获取菜单数据权限
     * 
     * @param user 用户对象
     * @return 菜单权限信息
     */
    @Override
    public Set<String> getMenuPermission(SysUser user) {
        Set<String> menuPerms = new HashSet<String>();
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            for (SysRole role : user.getRoles()) {
                List<String> perms = sysMenuMapper.selectMenuPermsByRoleId(role.getRoleId());
                if (perms != null) {
                    menuPerms.addAll(perms);
                }
            }
        }
        return menuPerms;
    }
}
