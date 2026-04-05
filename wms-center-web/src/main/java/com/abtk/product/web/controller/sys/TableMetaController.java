package com.abtk.product.web.controller.sys;

import com.abtk.product.api.domain.request.sys.TableMetaQueryRequest;
import com.abtk.product.api.domain.request.sys.TableMetaRequest;
import com.abtk.product.api.domain.response.sys.TableMetaResponse;
import com.abtk.product.biz.sys.TableMetaBiz;
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
 * 表元数据管理Controller
 *
 * @author backend
 * @since 2026-04-06
 */
@Slf4j
@RestController
@RequestMapping("/api/system/meta/table")
public class TableMetaController extends BaseController {

    @Autowired
    private TableMetaBiz tableMetaBiz;

    @Autowired
    private I18nService i18nService;

    /**
     * 分页查询表元数据列表
     */
    @RequiresPermissions("system:meta:table:query")
    @GetMapping
    public R<TableDataInfo> list(TableMetaQueryRequest queryRequest) {
        log.info("表元数据列表查询参数: tableCode={}, tableName={}, module={}, status={}",
                queryRequest.getTableCode(), queryRequest.getTableName(),
                queryRequest.getModule(), queryRequest.getStatus());
        startPage();
        return R.ok(getDataTable(tableMetaBiz.list(queryRequest).getData()));
    }

    /**
     * 查询所有表元数据（不分页）
     */
    @RequiresPermissions("system:meta:table:query")
    @GetMapping("/all")
    public R listAll() {
        return tableMetaBiz.listAll();
    }

    /**
     * 根据ID查询表元数据详情
     */
    @RequiresPermissions("system:meta:table:query")
    @GetMapping("/{id}")
    public R getById(@PathVariable Long id) {
        return tableMetaBiz.queryById(id);
    }

    /**
     * 根据表编码查询表元数据
     */
    @RequiresPermissions("system:meta:table:query")
    @GetMapping("/code/{code}")
    public R getByTableCode(@PathVariable String code) {
        return tableMetaBiz.queryByTableCode(code);
    }

    /**
     * 创建表元数据
     */
    @Log(title = "表元数据管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:meta:table:manage")
    @PostMapping
    public R create(@RequestBody @Valid TableMetaRequest request) {
        return tableMetaBiz.add(request);
    }

    /**
     * 更新表元数据
     */
    @Log(title = "表元数据管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:meta:table:manage")
    @PutMapping("/{id}")
    public R update(@PathVariable Long id, @RequestBody @Valid TableMetaRequest request) {
        return tableMetaBiz.update(id, request);
    }

    /**
     * 删除表元数据
     */
    @Log(title = "表元数据管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:meta:table:manage")
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        return tableMetaBiz.delete(id);
    }

    /**
     * 切换表元数据状态（启用/禁用）
     */
    @Log(title = "表元数据管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:meta:table:manage")
    @PutMapping("/{id}/toggle")
    public R toggleStatus(@PathVariable Long id, @RequestParam Integer status) {
        return tableMetaBiz.toggleStatus(id, status);
    }

    /**
     * 导出表元数据Excel
     */
    @Log(title = "表元数据管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RequiresPermissions("system:meta:table:export")
    public void export(HttpServletResponse response, @RequestBody TableMetaQueryRequest request) {
        log.info("导出表元数据: {}", request);
        List<TableMetaResponse> list = tableMetaBiz.exportList(request);
        String fileName = i18nService.getMessage("export.filename.tableMeta");
        if (fileName == null || fileName.isEmpty()) {
            fileName = "表元数据_" + DateUtils.parseDateToStr("yyyyMMdd_HHmmss", new Date());
        }
        ExcelUtil<TableMetaResponse> util = new ExcelUtil<>(TableMetaResponse.class);
        util.exportExcel(response, list, fileName);
    }
}
