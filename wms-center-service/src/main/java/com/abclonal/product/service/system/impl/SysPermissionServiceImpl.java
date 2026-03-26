package com.abclonal.product.service.system.impl;

import com.abclonal.product.dao.entity.SysPermission;
import com.abclonal.product.dao.mapper.sys.SysPermissionMapper;
import com.abclonal.product.service.system.service.ISysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 权限管理表(SysPermission)表服务实现类
 *
 * @author backend
 * @since 2026-03-26
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysPermissionServiceImpl implements ISysPermissionService {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    // ==================== 查询方法 ====================

    @Override
    public SysPermission queryById(Long permissionId) {
        return sysPermissionMapper.queryById(permissionId);
    }

    @Override
    public List<SysPermission> queryByCondition(SysPermission permission) {
        return sysPermissionMapper.queryAll(permission);
    }

    @Override
    public List<SysPermission> listAll() {
        return sysPermissionMapper.listAll();
    }

    @Override
    public long count(SysPermission permission) {
        return sysPermissionMapper.count(permission);
    }

    @Override
    public boolean checkPermissionCodeUnique(String permissionCode) {
        return sysPermissionMapper.checkPermissionCodeUnique(permissionCode) == null;
    }

    @Override
    public boolean checkPermissionNameUnique(String permissionName, Long parentId) {
        return sysPermissionMapper.checkPermissionNameUnique(permissionName, parentId) == null;
    }

    // ==================== 新增方法 ====================

    @Override
    public SysPermission insert(SysPermission permission) {
        Date now = new Date();
        if (permission.getCreateTime() == null) {
            permission.setCreateTime(now);
        }
        if (permission.getUpdateTime() == null) {
            permission.setUpdateTime(now);
        }
        if (permission.getDelFlag() == null) {
            permission.setDelFlag("0");
        }
        if (permission.getStatus() == null) {
            permission.setStatus("0");
        }
        sysPermissionMapper.insert(permission);
        return permission;
    }

    // ==================== 修改方法 ====================

    @Override
    public int update(SysPermission permission) {
        permission.setUpdateTime(new Date());
        return sysPermissionMapper.update(permission);
    }

    // ==================== 删除方法 ====================

    @Override
    public boolean logicDeleteById(Long permissionId, String username) {
        return sysPermissionMapper.deleteById(permissionId, username) > 0;
    }

    @Override
    public void toggleStatus(Long permissionId, String status) {
        sysPermissionMapper.toggleStatus(permissionId, status);
    }
}
