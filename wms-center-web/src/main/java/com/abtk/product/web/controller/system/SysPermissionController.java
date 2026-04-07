package com.abtk.product.web.controller.system;

import com.abtk.product.api.domain.request.sys.SysPermissionRequest;
import com.abtk.product.api.domain.response.sys.SysPermissionResponse;
import com.abtk.product.biz.sys.SysPermissionBiz;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.log.enums.BusinessType;
import com.abtk.product.common.web.controller.BaseController;
import com.abtk.product.common.web.page.TableDataInfo;
import com.abtk.product.service.annotation.Log;
import com.abtk.product.web.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.abtk.product.common.utils.PageUtils.startPage;

/**
 * 权限管理Controller（/api/system/permission）
 */
@RestController
@RequestMapping("/api/system/permission")
public class SysPermissionController extends BaseController {

    @Autowired
    private SysPermissionBiz sysPermissionBiz;

    /**
     * 分页查询权限列表
     */
    @RequiresPermissions("system:permission:list")
    @GetMapping("/list")
    public R<TableDataInfo> list(SysPermissionRequest queryRequest) {
        startPage();
        return R.ok(getDataTable(sysPermissionBiz.list(queryRequest).getData()));
    }

    /**
     * 查询所有权限（不分页）
     */
    @RequiresPermissions("system:permission:list")
    @GetMapping("/listAll")
    public R<List<SysPermissionResponse>> listAll() {
        return sysPermissionBiz.listAll();
    }

    /**
     * 根据ID查询权限详情
     */
    @RequiresPermissions("system:permission:list")
    @GetMapping("/{id}")
    public R<SysPermissionResponse> getById(@PathVariable("id") Long id) {
        return sysPermissionBiz.queryById(id);
    }

    /**
     * 新增权限
     */
    @Log(title = "权限管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:permission:add")
    @PostMapping
    public R<Long> create(@Valid @RequestBody SysPermissionRequest request) {
        return sysPermissionBiz.add(request);
    }

    /**
     * 编辑权限
     */
    @Log(title = "权限管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:permission:edit")
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable("id") Long id, @Valid @RequestBody SysPermissionRequest request) {
        return sysPermissionBiz.update(id, request);
    }

    /**
     * 删除权限
     */
    @Log(title = "权限管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:permission:delete")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable("id") Long id) {
        return sysPermissionBiz.delete(id);
    }

    /**
     * 切换权限状态
     */
    @Log(title = "权限管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:permission:edit")
    @PatchMapping("/{id}/status")
    public R<Void> toggleStatus(@PathVariable("id") Long id, @RequestParam("enabled") Integer enabled) {
        return sysPermissionBiz.toggleStatus(id, enabled);
    }
}
