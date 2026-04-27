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
        if (user == null) {
            return rolePerms;
        }
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            for (SysRole role : user.getRoles()) {
                if (role != null && role.getRoleKey() != null && !role.getRoleKey().isEmpty()) {
                    rolePerms.add(role.getRoleKey());
                }
            }
        }
        // 与 AuthLogic.SUPER_ADMIN 一致：内置超管 ID 或登录名 admin 保证具备 admin 角色标识（前端/鉴权）
        if (SysUser.isAdmin(user.getUserId())
                || (user.getUserName() != null && "admin".equalsIgnoreCase(user.getUserName().trim()))) {
            rolePerms.add("admin");
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
        if (user == null) {
            return menuPerms;
        }
        // 与 AuthLogic.ALL_PERMISSION 一致：超级管理员或内置 admin 角色拥有全部接口权限，
        // 避免因 sys_role_menu 未配置、迁移后用户-角色关联异常导致仅登录无权限（403）
        if (SysUser.isAdmin(user.getUserId())) {
            menuPerms.add("*:*:*");
            return menuPerms;
        }
        if (user.getUserName() != null && "admin".equalsIgnoreCase(user.getUserName().trim())) {
            menuPerms.add("*:*:*");
            return menuPerms;
        }
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            for (SysRole role : user.getRoles()) {
                if (role != null && "admin".equals(role.getRoleKey())) {
                    menuPerms.add("*:*:*");
                    return menuPerms;
                }
            }
            for (SysRole role : user.getRoles()) {
                List<String> perms = sysMenuMapper.selectMenuPermsByRoleId(role.getId());
                if (perms != null) {
                    menuPerms.addAll(perms);
                }
            }
        }
        return menuPerms;
    }
}
