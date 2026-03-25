package com.abclonal.product.web.controller.system;

import com.abclonal.product.api.domain.request.sys.SysRoleRequest;
import com.abclonal.product.api.domain.response.sys.SysRoleResponse;
import com.abclonal.product.common.domain.R;
import com.abclonal.product.common.web.controller.BaseController;
import com.abclonal.product.common.web.page.TableDataInfo;
import com.abclonal.product.common.log.enums.BusinessType;
import com.abclonal.product.service.annotation.Log;
import com.abclonal.product.service.security.utils.SecurityUtils;
import com.abclonal.product.service.system.service.ISysRoleService;
import com.abclonal.product.web.security.annotation.RequiresPermissions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理 Controller
 *
 * @author lht
 * @since 2026-03-11
 */
@RestController
@RequestMapping("/api/system/role")
@Tag(name = "角色管理", description = "系统角色管理相关接口")
public class SysRoleController extends BaseController {

    @Autowired
    private ISysRoleService roleService;

    /**
     * 查询角色列表
     */
    @RequiresPermissions("system:role:list")
    @Operation(summary = "查询角色列表")
    @GetMapping("/list")
    public R<TableDataInfo> list(SysRoleRequest request) {
        startPage();
        List<SysRoleResponse> list = roleService.selectRoleList(request);
        return R.ok(getDataTable(list));
    }

    /**
     * 查询所有角色（用于下拉选择）
     */
    @RequiresPermissions("system:role:list")
    @Operation(summary = "查询所有角色")
    @GetMapping("/all")
    public R<List<SysRoleResponse>> all() {
        return R.ok(roleService.selectRoleAll());
    }

    /**
     * 根据角色ID查询详情
     */
    @RequiresPermissions("system:role:query")
    @Operation(summary = "根据ID查询角色详情")
    @GetMapping(value = "/{roleId}")
    public R<SysRoleResponse> getInfo(
            @Parameter(description = "角色ID", required = true) @PathVariable Long roleId) {
        return R.ok(roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     */
    @RequiresPermissions("system:role:add")
    @Operation(summary = "新增角色")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@RequestBody SysRoleRequest request) {
        if (!roleService.checkRoleNameUnique(request)) {
            return R.fail("新增角色'" + request.getRoleName() + "'失败，角色名称已存在");
        } else if (!roleService.checkRoleKeyUnique(request)) {
            return R.fail("新增角色'" + request.getRoleName() + "'失败，角色权限已存在");
        }
        String username = SecurityUtils.getUsername();
        return R.ok(roleService.insertRole(request, username) > 0 ? "新增成功" : "新增失败");
    }

    /**
     * 修改角色
     */
    @RequiresPermissions("system:role:edit")
    @Operation(summary = "修改角色")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<String> edit(@RequestBody SysRoleRequest request) {
        if (!roleService.checkRoleNameUnique(request)) {
            return R.fail("修改角色'" + request.getRoleName() + "'失败，角色名称已存在");
        } else if (!roleService.checkRoleKeyUnique(request)) {
            return R.fail("修改角色'" + request.getRoleName() + "'失败，角色权限已存在");
        }
        String username = SecurityUtils.getUsername();
        return R.ok(roleService.updateRole(request, username) > 0 ? "修改成功" : "修改失败");
    }

    /**
     * 删除角色
     */
    @RequiresPermissions("system:role:delete")
    @Operation(summary = "删除角色")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public R<String> remove(
            @Parameter(description = "角色ID串", required = true) @PathVariable Long[] roleIds) {
        String username = SecurityUtils.getUsername();
        return R.ok(roleService.deleteRoleByIds(roleIds, username) > 0 ? "删除成功" : "删除失败");
    }

    /**
     * 修改角色状态
     */
    @RequiresPermissions("system:role:edit")
    @Operation(summary = "修改角色状态")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public R<String> changeStatus(@RequestBody SysRoleRequest request) {
        String username = SecurityUtils.getUsername();
        return R.ok(roleService.updateRoleStatus(request, username) > 0 ? "修改成功" : "修改失败");
    }

    /**
     * 分配角色菜单权限
     */
    @RequiresPermissions("system:role:assign")
    @Operation(summary = "分配角色菜单权限")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/assignMenu")
    public R<String> assignMenu(@RequestBody SysRoleRequest request) {
        String username = SecurityUtils.getUsername();
        return R.ok(roleService.assignRoleMenu(request, username) > 0 ? "分配成功" : "分配失败");
    }

    /**
     * 根据用户ID查询角色列表
     */
    @Operation(summary = "根据用户ID查询角色列表")
    @GetMapping("/user/{userId}")
    public R<List<SysRoleResponse>> getRolesByUserId(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        return R.ok(roleService.selectRolesByUserId(userId));
    }
}
