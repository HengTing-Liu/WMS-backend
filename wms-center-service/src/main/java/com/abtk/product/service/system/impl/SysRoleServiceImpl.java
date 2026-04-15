package com.abtk.product.service.system.impl;

import com.abtk.product.api.domain.request.sys.SysRoleRequest;
import com.abtk.product.api.domain.response.sys.SysRoleResponse;
import com.abtk.product.dao.entity.SysRole;
import com.abtk.product.dao.entity.SysRoleDept;
import com.abtk.product.dao.entity.SysRoleMenu;
import com.abtk.product.dao.entity.SysUserRole;
import com.abtk.product.dao.mapper.SysRoleMapper;
import com.abtk.product.dao.mapper.SysRoleMenuMapper;
import com.abtk.product.dao.mapper.SysRoleDeptMapper;
import com.abtk.product.dao.mapper.SysUserRoleMapper;
import com.abtk.product.domain.converter.SysRoleConverter;
import com.abtk.product.service.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色管理 Service 实现
 *
 * @author lht
 * @since 2026-03-11
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService {

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired
    private SysRoleDeptMapper roleDeptMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Override
    public List<SysRoleResponse> selectRoleList(SysRoleRequest request) {
        SysRole role = SysRoleConverter.INSTANCE.requestToEntity(request);
        List<SysRole> list = roleMapper.selectRoleList(role);
        return SysRoleConverter.INSTANCE.entityListToResponseList(list);
    }

    @Override
    public SysRoleResponse selectRoleById(Long id) {
        SysRole role = roleMapper.selectRoleById(id);
        SysRoleResponse response = SysRoleConverter.INSTANCE.entityToResponse(role);
        if (response != null) {
            // 查询角色关联的菜单ID
            List<Long> menuIds = roleMenuMapper.selectMenuIdsByRoleId(id);
            response.setMenuIds(menuIds.toArray(new Long[0]));
            // 查询角色关联的部门ID（数据权限自定义时使用）
            List<Long> deptIds = roleDeptMapper.selectDeptIdsByRoleId(id);
            response.setDeptIds(deptIds.toArray(new Long[0]));
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertRole(SysRoleRequest request, String createBy) {
        SysRole role = SysRoleConverter.INSTANCE.requestToEntity(request);
        role.setCreateBy(createBy);
        role.setCreateTime(new Date());
        role.setDelFlag("0");
        int rows = roleMapper.insertRole(role);
        // 插入角色菜单关联
        insertRoleMenu(role.getId(), request.getMenuIds());
        // 插入角色部门关联（数据权限自定义时使用）
        insertRoleDept(role.getId(), request.getDeptIds());
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRole(SysRoleRequest request, String updateBy) {
        SysRole role = SysRoleConverter.INSTANCE.requestToEntity(request);
        role.setUpdateBy(updateBy);
        role.setUpdateTime(new Date());
        int rows = roleMapper.updateRole(role);
        // 删除旧的角色菜单关联，插入新的
        roleMenuMapper.deleteRoleMenuByRoleId(request.getId());
        insertRoleMenu(request.getId(), request.getMenuIds());
        // 删除旧的角色部门关联，插入新的
        roleDeptMapper.deleteRoleDeptByRoleId(request.getId());
        insertRoleDept(request.getId(), request.getDeptIds());
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRoleByIds(Long[] ids, String updateBy) {
        for (Long id : ids) {
            roleMenuMapper.deleteRoleMenuByRoleId(id);
            roleDeptMapper.deleteRoleDeptByRoleId(id);
        }
        return roleMapper.deleteRoleByIds(ids);
    }

    @Override
    public boolean checkRoleNameUnique(SysRoleRequest request) {
        SysRole role = roleMapper.checkRoleNameUnique(request.getRoleName());
        return role == null || role.getId().equals(request.getId());
    }

    @Override
    public boolean checkRoleKeyUnique(SysRoleRequest request) {
        SysRole role = roleMapper.checkRoleKeyUnique(request.getRoleKey());
        return role == null || role.getId().equals(request.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int assignRoleMenu(SysRoleRequest request, String updateBy) {
        roleMenuMapper.deleteRoleMenuByRoleId(request.getId());
        insertRoleMenu(request.getId(), request.getMenuIds());
        SysRole role = new SysRole();
        role.setId(request.getId());
        role.setUpdateBy(updateBy);
        role.setUpdateTime(new Date());
        // 保存菜单树选择项是否关联显示设置
        if (request.getMenuCheckStrictly() != null) {
            role.setMenuCheckStrictly(request.getMenuCheckStrictly());
        }
        return roleMapper.updateRole(role);
    }

    @Override
    public List<SysRoleResponse> selectRolesByUserId(Long userId) {
        List<SysRole> list = roleMapper.selectRolePermissionByUserId(userId);
        return SysRoleConverter.INSTANCE.entityListToResponseList(list);
    }

    @Override
    public List<SysRoleResponse> selectRoleAll() {
        List<SysRole> list = roleMapper.selectRoleAll();
        return SysRoleConverter.INSTANCE.entityListToResponseList(list);
    }

    @Override
    public int updateRoleStatus(SysRoleRequest request, String updateBy) {
        SysRole role = new SysRole();
        role.setId(request.getId());
        role.setStatus(request.getStatus());
        role.setUpdateBy(updateBy);
        role.setUpdateTime(new Date());
        return roleMapper.updateRole(role);
    }

    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        // 保持原有功能，返回权限字符串集合
        List<SysRole> roles = roleMapper.selectRolePermissionByUserId(userId);
        return roles.stream()
                .map(SysRole::getRoleKey)
                .collect(Collectors.toSet());
    }

    @Override
    public int countUserRoleByRoleId(Long roleId) {
        // 统计使用该角色的用户数量
        return 0; // TODO: 实现统计
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAuthUser(SysUserRole userRole) {
        return userRoleMapper.deleteUserRoleInfo(userRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAuthUsers(Long id, Long[] userIds) {
        return userRoleMapper.deleteUserRoleInfos(id, userIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertAuthUsers(Long id, Long[] userIds) {
        if (userIds == null || userIds.length == 0) {
            return 0;
        }
        List<SysUserRole> list = new java.util.ArrayList<>();
        for (Long userId : userIds) {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(userId);
            ur.setRoleId(id);
            list.add(ur);
        }
        return userRoleMapper.batchUserRole(list);
    }

    @Override
    public void checkRoleDataScope(Long... roleIds) {
        // 数据权限校验，暂时空实现
        // TODO: 根据当前用户的数据权限范围校验是否有权限操作这些角色
    }

    /**
     * 插入角色菜单关联
     */
    private void insertRoleMenu(Long id, Long[] menuIds) {
        if (menuIds != null && menuIds.length > 0 && id != null) {
            for (Long menuId : menuIds) {
                SysRoleMenu rm = new SysRoleMenu();
                rm.setRoleId(id);
                rm.setMenuId(menuId);
                roleMenuMapper.insertRoleMenu(rm);
            }
        }
    }

    /**
     * 插入角色部门关联（数据权限自定义时使用）
     */
    private void insertRoleDept(Long id, Long[] deptIds) {
        if (deptIds != null && deptIds.length > 0 && id != null) {
            List<SysRoleDept> list = new ArrayList<>();
            for (Long deptId : deptIds) {
                SysRoleDept rd = new SysRoleDept();
                rd.setRoleId(id);
                rd.setDeptId(deptId);
                list.add(rd);
            }
            roleDeptMapper.batchRoleDept(list);
        }
    }
}
