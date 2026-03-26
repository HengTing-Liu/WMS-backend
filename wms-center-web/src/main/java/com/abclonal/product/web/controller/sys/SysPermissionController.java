package com.abclonal.product.web.controller.sys;

import com.abclonal.product.api.domain.request.sys.SysPermissionRequest;
import com.abclonal.product.api.domain.response.sys.SysPermissionResponse;
import com.abclonal.product.biz.sys.SysPermissionBiz;
import com.abclonal.product.common.domain.R;
import com.abclonal.product.common.log.enums.BusinessType;
import com.abclonal.product.common.web.controller.BaseController;
import com.abclonal.product.common.web.page.TableDataInfo;
import com.abclonal.product.service.annotation.Log;
import com.abclonal.product.web.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.abclonal.product.common.utils.PageUtils.startPage;

/**
 * 权限管理Controller（/api/base/permission）
 *
 * @author backend
 * @since 2026-03-26
 */
@RestController
@RequestMapping("/api/base/permission")
public class SysPermissionController extends BaseController {

    @Autowired
    private SysPermissionBiz sysPermissionBiz;

    /**
     * 分页查询权限列表
     */
    @RequiresPermissions("wms:base:permission:list")
    @GetMapping("/list")
    public R<TableDataInfo> list(SysPermissionRequest queryRequest) {
        startPage();
        return R.ok(getDataTable(sysPermissionBiz.list(queryRequest).getData()));
    }

    /**
     * 查询所有权限（不分页）
     */
    @RequiresPermissions("wms:base:permission:list")
    @GetMapping("/listAll")
    public R<List<SysPermissionResponse>> listAll() {
        return sysPermissionBiz.listAll();
    }

    /**
     * 根据ID查询权限详情
     */
    @RequiresPermissions("wms:base:permission:list")
    @GetMapping("/{id}")
    public R<SysPermissionResponse> getById(@PathVariable("id") Long id) {
        return sysPermissionBiz.queryById(id);
    }

    /**
     * 新增权限
     */
    @Log(title = "权限管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("wms:base:permission:add")
    @PostMapping
    public R<Long> create(@Valid @RequestBody SysPermissionRequest request) {
        return sysPermissionBiz.add(request);
    }

    /**
     * 编辑权限
     */
    @Log(title = "权限管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("wms:base:permission:edit")
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable("id") Long id, @Valid @RequestBody SysPermissionRequest request) {
        return sysPermissionBiz.update(id, request);
    }

    /**
     * 删除权限
     */
    @Log(title = "权限管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("wms:base:permission:delete")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable("id") Long id) {
        return sysPermissionBiz.delete(id);
    }

    /**
     * 切换权限状态
     */
    @Log(title = "权限管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("wms:base:permission:edit")
    @PatchMapping("/{id}/status")
    public R<Void> toggleStatus(@PathVariable("id") Long id, @RequestParam("enabled") Integer enabled) {
        return sysPermissionBiz.toggleStatus(id, enabled);
    }
}
