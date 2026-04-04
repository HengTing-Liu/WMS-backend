package com.abtk.product.biz.sys;

import com.abtk.product.api.domain.request.sys.SysPermissionRequest;
import com.abtk.product.api.domain.response.sys.SysPermissionResponse;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.utils.bean.BeanUtils;
import com.abtk.product.dao.entity.SysPermission;
import com.abtk.product.service.system.service.I18nService;
import com.abtk.product.service.system.service.ISysPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 权限管理业务层
 * 复杂业务逻辑在此层处理，Service层只做简单CRUD
 *
 * @author backend
 * @since 2026-03-26
 */
@Slf4j
@Component
public class SysPermissionBiz {

    @Autowired
    private ISysPermissionService sysPermissionService;

    @Autowired
    private I18nService i18nService;

    // ==================== 基础CRUD方法 ====================

    /**
     * 分页查询
     */
    public R<List<SysPermissionResponse>> list(SysPermissionRequest request) {
        SysPermission condition = new SysPermission();
        BeanUtils.copyProperties(request, condition);
        List<SysPermission> list = sysPermissionService.queryByCondition(condition);
        List<SysPermissionResponse> responseList = convertToResponseList(list);
        return R.ok(responseList);
    }

    /**
     * 查询所有（不分页）
     */
    public R<List<SysPermissionResponse>> listAll() {
        List<SysPermission> list = sysPermissionService.listAll();
        List<SysPermissionResponse> responseList = convertToResponseList(list);
        return R.ok(responseList);
    }

    /**
     * 通过ID查询
     */
    public R<SysPermissionResponse> queryById(Long permissionId) {
        SysPermission entity = sysPermissionService.queryById(permissionId);
        if (entity == null) {
            return R.fail(i18nService.getMessage("wms.permission.not.found"));
        }
        return R.ok(convertToResponse(entity));
    }

    /**
     * 新增数据
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Long> add(SysPermissionRequest request) {
        // 校验权限编码唯一性
        if (!sysPermissionService.checkPermissionCodeUnique(request.getPermissionCode())) {
            return R.fail("权限编码已存在");
        }
        // 校验权限名称唯一性
        Long parentId = request.getParentId() != null ? request.getParentId() : 0L;
        if (!sysPermissionService.checkPermissionNameUnique(request.getPermissionName(), parentId)) {
            return R.fail("权限名称已存在");
        }

        SysPermission entity = convertToEntity(request);
        fillSystemFields(entity);

        SysPermission result = sysPermissionService.insert(entity);
        return R.ok(result.getPermissionId());
    }

    /**
     * 编辑数据
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Void> update(Long permissionId, SysPermissionRequest request) {
        SysPermission existing = sysPermissionService.queryById(permissionId);
        if (existing == null) {
            return R.fail(i18nService.getMessage("wms.permission.not.found"));
        }

        // 如果修改了权限编码，校验唯一性
        if (request.getPermissionCode() != null
                && !request.getPermissionCode().equals(existing.getPermissionCode())) {
            if (!sysPermissionService.checkPermissionCodeUnique(request.getPermissionCode())) {
                return R.fail("权限编码已存在");
            }
        }

        // 如果修改了权限名称，校验唯一性
        if (request.getPermissionName() != null
                && !request.getPermissionName().equals(existing.getPermissionName())) {
            Long parentId = request.getParentId() != null ? request.getParentId() : existing.getParentId();
            if (!sysPermissionService.checkPermissionNameUnique(request.getPermissionName(), parentId)) {
                return R.fail("权限名称已存在");
            }
        }

        SysPermission entity = convertToEntity(request);
        entity.setPermissionId(permissionId);
        entity.setUpdateTime(new Date());

        sysPermissionService.update(entity);
        return R.ok();
    }

    /**
     * 删除数据
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Void> delete(Long permissionId) {
        SysPermission existing = sysPermissionService.queryById(permissionId);
        if (existing == null) {
            return R.fail(i18nService.getMessage("wms.permission.not.found"));
        }

        sysPermissionService.logicDeleteById(permissionId, "system");
        return R.ok();
    }

    /**
     * 切换状态
     */
    public R<Void> toggleStatus(Long permissionId, Integer enabled) {
        SysPermission existing = sysPermissionService.queryById(permissionId);
        if (existing == null) {
            return R.fail(i18nService.getMessage("wms.permission.not.found"));
        }

        String status = (enabled != null && enabled == 1) ? "0" : "1";
        sysPermissionService.toggleStatus(permissionId, status);
        return R.ok();
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 填充系统字段
     */
    private void fillSystemFields(SysPermission entity) {
        Date now = new Date();
        entity.setCreateBy("system");
        entity.setUpdateBy("system");
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        entity.setDelFlag("0");
    }

    /**
     * Entity 转 Response
     */
    private SysPermissionResponse convertToResponse(SysPermission entity) {
        if (entity == null) {
            return null;
        }
        SysPermissionResponse response = new SysPermissionResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    /**
     * Entity列表转Response列表
     */
    private List<SysPermissionResponse> convertToResponseList(List<SysPermission> list) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        List<SysPermissionResponse> result = new ArrayList<>();
        for (SysPermission entity : list) {
            result.add(convertToResponse(entity));
        }
        return result;
    }

    /**
     * Request 转 Entity
     */
    private SysPermission convertToEntity(SysPermissionRequest request) {
        if (request == null) {
            return null;
        }
        SysPermission entity = new SysPermission();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }
}
