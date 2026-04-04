package com.abtk.product.web.controller.sys;

import com.abtk.product.api.domain.request.sys.WarehouseQueryRequest;
import com.abtk.product.api.domain.request.sys.WarehouseRequest;
import com.abtk.product.api.domain.response.sys.WarehouseResponse;
import com.abtk.product.biz.sys.WarehouseBiz;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.log.enums.BusinessType;
import com.abtk.product.common.utils.DateUtils;
import com.abtk.product.common.utils.poi.ExcelUtil;
import com.abtk.product.common.web.controller.BaseController;
import com.abtk.product.common.web.page.TableDataInfo;
import com.abtk.product.service.annotation.Log;
import com.abtk.product.service.system.service.I18nService;
import com.abtk.product.web.security.annotation.RequiresPermissions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
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

    @Autowired
    private I18nService i18nService;

    /**
     * 分页查询仓库列表
     */
    @RequiresPermissions("wms:base:warehouse:query")
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
    @RequiresPermissions("wms:base:warehouse:query")
    @GetMapping("/listAll")
    public R listAll() {
        return warehouseBiz.listAll();
    }

    /**
     * 根据ID查询仓库详情
     */
    @RequiresPermissions("wms:base:warehouse:query")
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

    /**
     * 导出仓库档案Excel
     */
    @Log(title = "仓库档案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RequiresPermissions("wms:base:warehouse:export")
    public void export(HttpServletResponse response, @RequestBody WarehouseQueryRequest request) {
        log.info("导出仓库档案: {}", request);
        List<WarehouseResponse> list = warehouseBiz.exportList(request);
        String fileName = i18nService.getMessage("export.filename.warehouse");
        if (fileName == null || fileName.isEmpty()) {
            fileName = "仓库档案_" + DateUtils.parseDateToStr("yyyyMMdd_HHmmss", new Date());
        }
        ExcelUtil<WarehouseResponse> util = new ExcelUtil<>(WarehouseResponse.class);
        util.exportExcel(response, list, fileName);
    }
}