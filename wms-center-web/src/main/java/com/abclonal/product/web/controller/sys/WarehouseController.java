package com.abclonal.product.web.controller.sys;

import com.abclonal.product.api.domain.request.sys.WarehouseQueryRequest;
import com.abclonal.product.api.domain.request.sys.WarehouseRequest;
import com.abclonal.product.biz.sys.WarehouseBiz;
import com.abclonal.product.common.domain.R;
import com.abclonal.product.common.log.enums.BusinessType;
import com.abclonal.product.common.web.controller.BaseController;
import com.abclonal.product.common.web.page.TableDataInfo;
import com.abclonal.product.service.annotation.Log;
import com.abclonal.product.web.security.annotation.RequiresPermissions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 仓库档案Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/base/warehouse")
public class WarehouseController extends BaseController {

    @Autowired
    private WarehouseBiz warehouseBiz;

    /**
     * 分页查询仓库列表
     */
    @RequiresPermissions("wms:base:warehouse:list")
    @GetMapping("/list")
    public R<TableDataInfo> list(WarehouseQueryRequest queryRequest) {
        log.info("仓库列表查询参数: warehouseCode={}, warehouseName={}, company={}, isEnabled={}",
                queryRequest.getWarehouseCode(), queryRequest.getWarehouseName(),
                queryRequest.getCompany(), queryRequest.getIsEnabled());
        startPage();
        return R.ok(getDataTable(warehouseBiz.list(queryRequest).getData()));
    }

    /**
     * 查询所有仓库（不分页）
     */
    @RequiresPermissions("wms:base:warehouse:list")
    @GetMapping("/listAll")
    public R listAll() {
        return warehouseBiz.listAll();
    }

    /**
     * 根据ID查询仓库详情
     */
    @RequiresPermissions("wms:base:warehouse:list")
    @GetMapping("/{id}")
    public R getById(@PathVariable Long id) {
        return warehouseBiz.queryById(id);
    }

    /**
     * 新增仓库
     */
    @Log(title = "仓库档案", businessType = BusinessType.INSERT)
    @RequiresPermissions("wms:base:warehouse:add")
    @PostMapping
    public R create(@RequestBody @Valid WarehouseRequest request) {
        return warehouseBiz.add(request);
    }

    /**
     * 编辑仓库
     */
    @Log(title = "仓库档案", businessType = BusinessType.UPDATE)
    @RequiresPermissions("wms:base:warehouse:edit")
    @PutMapping("/{id}")
    public R update(@PathVariable Long id, @RequestBody @Valid WarehouseRequest request) {
        return warehouseBiz.update(id, request);
    }

    /**
     * 删除仓库
     */
    @Log(title = "仓库档案", businessType = BusinessType.DELETE)
    @RequiresPermissions("wms:base:warehouse:delete")
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        return warehouseBiz.delete(id);
    }

    /**
     * 切换仓库状态
     */
    @Log(title = "仓库档案", businessType = BusinessType.UPDATE)
    @RequiresPermissions("wms:base:warehouse:edit")
    @PatchMapping("/{id}/status")
    public R toggleStatus(@PathVariable Long id, @RequestParam Integer enabled) {
        return warehouseBiz.toggleStatus(id, enabled);
    }

    /**
     * 查询所有不重复的公司列表
     */
    @GetMapping("/companyList")
    public R<List<String>> getCompanyList() {
        return warehouseBiz.listDistinctCompany();
    }
}