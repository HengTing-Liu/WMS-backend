package com.abtk.product.web.controller.sys;

import com.abtk.product.api.domain.request.sys.StorageQueryRequest;
import com.abtk.product.api.domain.request.sys.StorageRequest;
import com.abtk.product.biz.sys.StorageBiz;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.log.enums.BusinessType;
import com.abtk.product.common.web.controller.BaseController;
import com.abtk.product.common.web.page.TableDataInfo;
import com.abtk.product.service.annotation.Log;
import com.abtk.product.web.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 库区档案Controller
 * WMS0050 库区管理
 */
@RestController
@RequestMapping("/api/base/storage")
public class StorageController extends BaseController {

    @Autowired
    private StorageBiz storageBiz;

    /**
     * 分页查询库区列表
     */
    @RequiresPermissions("wms:base:storage:list")
    @GetMapping("/list")
    public R<TableDataInfo> list(StorageQueryRequest queryRequest) {
        startPage();
        return R.ok(getDataTable(storageBiz.list(queryRequest).getData()));
    }

    /**
     * 查询所有库区（不分页）
     */
    @RequiresPermissions("wms:base:storage:list")
    @GetMapping("/listAll")
    public R listAll() {
        return storageBiz.listAll();
    }

    /**
     * 根据ID查询库区详情
     */
    @RequiresPermissions("wms:base:storage:list")
    @GetMapping("/{id}")
    public R getById(@PathVariable Long id) {
        return storageBiz.queryById(id);
    }

    /**
     * 新增库区
     */
    @Log(title = "库区档案", businessType = BusinessType.INSERT)
    @RequiresPermissions("wms:base:storage:add")
    @PostMapping
    public R create(@RequestBody @Valid StorageRequest request) {
        return storageBiz.add(request);
    }

    /**
     * 编辑库区
     */
    @Log(title = "库区档案", businessType = BusinessType.UPDATE)
    @RequiresPermissions("wms:base:storage:edit")
    @PutMapping("/{id}")
    public R update(@PathVariable Long id, @RequestBody @Valid StorageRequest request) {
        return storageBiz.update(id, request);
    }

    /**
     * 删除库区
     */
    @Log(title = "库区档案", businessType = BusinessType.DELETE)
    @RequiresPermissions("wms:base:storage:delete")
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        return storageBiz.delete(id);
    }

    /**
     * 切换库区状态
     */
    @Log(title = "库区档案", businessType = BusinessType.UPDATE)
    @RequiresPermissions("wms:base:storage:edit")
    @PatchMapping("/{id}/status")
    public R toggleStatus(@PathVariable Long id, @RequestParam Integer enabled) {
        return storageBiz.toggleStatus(id, enabled);
    }
}
