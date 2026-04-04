package com.abtk.product.web.controller.user;

import com.abtk.product.api.domain.request.user.WmsUserRequest;
import com.abtk.product.api.domain.response.user.WmsUserResponse;
import com.abtk.product.biz.user.WmsUserBiz;
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
 * 用户档案Controller（/api/base/user）
 *
 * @author backend
 * @since 2026-03-26
 */
@RestController
@RequestMapping("/api/base/user")
public class WmsUserController extends BaseController {

    @Autowired
    private WmsUserBiz wmsUserBiz;

    /**
     * 分页查询用户列表
     */
    @RequiresPermissions("wms:base:user:list")
    @GetMapping("/list")
    public R<TableDataInfo> list(WmsUserRequest queryRequest) {
        startPage();
        return R.ok(getDataTable(wmsUserBiz.list(queryRequest).getData()));
    }

    /**
     * 查询所有用户（不分页）
     */
    @RequiresPermissions("wms:base:user:list")
    @GetMapping("/listAll")
    public R<List<WmsUserResponse>> listAll() {
        return wmsUserBiz.listAll();
    }

    /**
     * 根据ID查询用户详情
     */
    @RequiresPermissions("wms:base:user:list")
    @GetMapping("/{id}")
    public R<WmsUserResponse> getById(@PathVariable("id") Long id) {
        return wmsUserBiz.queryById(id);
    }

    /**
     * 新增用户
     */
    @Log(title = "用户档案", businessType = BusinessType.INSERT)
    @RequiresPermissions("wms:base:user:add")
    @PostMapping
    public R<Long> create(@Valid @RequestBody WmsUserRequest request) {
        return wmsUserBiz.add(request);
    }

    /**
     * 编辑用户
     */
    @Log(title = "用户档案", businessType = BusinessType.UPDATE)
    @RequiresPermissions("wms:base:user:edit")
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable("id") Long id, @Valid @RequestBody WmsUserRequest request) {
        return wmsUserBiz.update(id, request);
    }

    /**
     * 删除用户
     */
    @Log(title = "用户档案", businessType = BusinessType.DELETE)
    @RequiresPermissions("wms:base:user:delete")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable("id") Long id) {
        return wmsUserBiz.delete(id);
    }

    /**
     * 切换用户状态
     */
    @Log(title = "用户档案", businessType = BusinessType.UPDATE)
    @RequiresPermissions("wms:base:user:edit")
    @PatchMapping("/{id}/status")
    public R<Void> toggleStatus(@PathVariable("id") Long id, @RequestParam("enabled") Integer enabled) {
        return wmsUserBiz.toggleStatus(id, enabled);
    }
}
