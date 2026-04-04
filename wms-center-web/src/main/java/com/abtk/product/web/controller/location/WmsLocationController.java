package com.abtk.product.web.controller.location;

import com.abtk.product.api.domain.request.location.WmsLocationBatchCreateRequest;
import com.abtk.product.api.domain.request.location.WmsLocationBatchRequest;
import com.abtk.product.api.domain.request.location.WmsLocationGridUpdateRequest;
import com.abtk.product.api.domain.request.location.WmsLocationRequest;
import com.abtk.product.api.domain.request.location.WmsLocationTreeRequest;
import com.abtk.product.api.domain.response.location.WmsLocationOccupancyResponse;
import com.abtk.product.api.domain.response.location.WmsLocationResponse;
import com.abtk.product.biz.location.WmsLocationBiz;
import com.abtk.product.common.domain.R;
import com.abtk.product.dao.entity.WmsLocationGridConfig;
import com.abtk.product.common.log.enums.BusinessType;
import com.abtk.product.common.utils.poi.ExcelUtil;
import com.abtk.product.common.web.controller.BaseController;
import com.abtk.product.common.web.page.TableDataInfo;
import com.abtk.product.service.annotation.Log;
import com.abtk.product.web.security.annotation.RequiresPermissions;
import com.abtk.product.service.system.service.I18nService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * 库位档案表(WmsLocation)控制层
 *
 * @author wms
 * @since 2026-03-14
 */
@Slf4j
@RestController
@RequestMapping("/api/wms/location")
@Tag(name = "库位管理", description = "库位档案管理相关接口")
public class WmsLocationController extends BaseController {

    @Autowired
    private WmsLocationBiz wmsLocationBiz;

    @Autowired
    private I18nService i18nService;

    // ==================== 基础CRUD接口 ====================

    /**
     * 分页查询
     */
    @GetMapping("/list")
    @RequiresPermissions("wms:location:list")
    @Operation(summary = "分页查询库位列表", description = "支持按条件分页查询库位档案")
    public R<TableDataInfo<WmsLocationResponse>> list(WmsLocationRequest request) {
        return R.ok(wmsLocationBiz.list(request));
    }

    /**
     * 通过ID查询单条数据
     */
    @GetMapping("/{id}")
    @RequiresPermissions("wms:location:query")
    @Operation(summary = "根据ID查询库位", description = "查询指定ID的库位详情")
    @Parameter(name = "id", description = "库位ID", required = true)
    public R<WmsLocationResponse> queryById(@PathVariable("id") Long id) {
        return wmsLocationBiz.queryById(id);
    }

    /**
     * 查询子节点列表（懒加载）
     */
    @GetMapping("/children/{parentId}")
    @RequiresPermissions("wms:location:list")
    @Operation(summary = "查询子节点", description = "根据父ID查询子节点列表，用于树形懒加载")
    @Parameter(name = "parentId", description = "父节点ID", required = true)
    public R<List<WmsLocationResponse>> queryChildren(@PathVariable("parentId") Long parentId) {
        return wmsLocationBiz.queryChildren(parentId);
    }

    /**
     * 新增数据
     */
    @PostMapping({"/add", ""})
    @RequiresPermissions("wms:location:add")
    @Operation(summary = "新增库位", description = "新增单个库位档案")
    public R<WmsLocationResponse> add(@Valid @RequestBody WmsLocationRequest request) {
        return wmsLocationBiz.add(request);
    }

    /**
     * 编辑数据
     */
    @PutMapping({"/update", "/{id}"})
    @RequiresPermissions("wms:location:edit")
    @Operation(summary = "更新库位", description = "更新库位档案信息")
    public R<Integer> update(@PathVariable(value = "id", required = false) Long id, @Valid @RequestBody WmsLocationRequest request) {
        if (id != null) {
            request.setId(id);
        }
        return wmsLocationBiz.update(request);
    }

    /**
     * 删除数据
     */
    @DeleteMapping({"/delete/{id}", "/{id}"})
    @RequiresPermissions("wms:location:delete")
    @Operation(summary = "删除库位", description = "逻辑删除指定库位（无子节点时允许删除）")
    @Parameter(name = "id", description = "库位ID", required = true)
    public R<Boolean> deleteById(@PathVariable("id") Long id) {
        return wmsLocationBiz.deleteById(id);
    }

    /**
     * 递归删除（删除节点及其所有子节点）
     */
    @DeleteMapping("/deleteRecursive/{id}")
    @RequiresPermissions("wms:location:delete")
    @Operation(summary = "递归删除库位", description = "删除指定节点及其所有子节点")
    @Parameter(name = "id", description = "库位ID", required = true)
    public R<Boolean> deleteRecursive(@PathVariable("id") Long id) {
        return wmsLocationBiz.deleteRecursive(id);
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/deleteBatch")
    @RequiresPermissions("wms:location:delete")
    @Operation(summary = "批量删除库位", description = "批量逻辑删除库位")
    public R<Boolean> deleteBatch(@RequestBody List<Long> ids) {
        return wmsLocationBiz.deleteBatch(ids);
    }

    // ==================== 批量操作接口 ====================

    /**
     * 批量新增
     */
    @PostMapping("/addBatch")
    @RequiresPermissions("wms:location:add")
    @Operation(summary = "批量新增库位", description = "批量新增库位档案，最多1000条")
    public R<Boolean> addBatch(@Valid @RequestBody WmsLocationBatchRequest batchRequest) {
        return wmsLocationBiz.addBatch(batchRequest);
    }

    /**
     * 批量更新
     */
    @PutMapping("/updateBatch")
    @RequiresPermissions("wms:location:edit")
    @Operation(summary = "批量更新库位", description = "批量更新库位档案，最多1000条")
    public R<Boolean> updateBatch(@Valid @RequestBody WmsLocationBatchRequest batchRequest) {
        return wmsLocationBiz.updateBatch(batchRequest);
    }

    /**
     * 批量创建（根据模板）
     */
    @PostMapping({"/batch-create", "/batchCreate", "/batch"})
    @RequiresPermissions("wms:location:add")
    @Operation(summary = "批量创建库位", description = "根据模板批量生成库位结构，如冰箱-层-架-行-盒-孔")
    public R<List<WmsLocationResponse>> batchCreate(@Valid @RequestBody WmsLocationBatchCreateRequest request) {
        return wmsLocationBiz.batchCreate(request);
    }

    // ==================== 树形结构接口 ====================

    /**
     * 查询树形结构
     */
    @GetMapping("/tree")
    @RequiresPermissions("wms:location:list")
    @Operation(summary = "查询库位树", description = "查询库位树形结构，支持递归和层级限制")
    public R<List<WmsLocationResponse>> queryTree(WmsLocationTreeRequest request) {
        return wmsLocationBiz.queryTree(request);
    }

    // ==================== 占用率统计接口 ====================

    /**
     * 查询占用率统计
     */
    @GetMapping("/occupancy/{locationId}")
    @RequiresPermissions("wms:location:query")
    @Operation(summary = "查询库位占用率", description = "查询指定库位的占用率统计信息")
    @Parameter(name = "locationId", description = "库位ID", required = true)
    public R<WmsLocationOccupancyResponse> queryOccupancy(@PathVariable("locationId") Long locationId) {
        return wmsLocationBiz.queryOccupancy(locationId);
    }

    // ==================== 网格配置接口 ====================

    /**
     * 查询网格配置
     */
    @GetMapping("/{id}/grid")
    @RequiresPermissions("wms:location:query")
    @Operation(summary = "查询网格配置", description = "查询容器类型的网格配置信息")
    @Parameter(name = "id", description = "库位ID", required = true)
    public R<WmsLocationGridConfig> queryGridConfig(@PathVariable("id") Long id) {
        return wmsLocationBiz.queryGridConfig(id);
    }

    /**
     * 更新网格配置
     */
    @PutMapping("/{id}/grid")
    @RequiresPermissions("wms:location:edit")
    @Operation(summary = "更新网格配置", description = "更新容器类型的网格配置")
    @Parameter(name = "id", description = "库位ID", required = true)
    public R<Boolean> updateGridConfig(@PathVariable("id") Long id, @Valid @RequestBody WmsLocationGridUpdateRequest request) {
        return wmsLocationBiz.updateGridConfig(id, request);
    }

    // ==================== 导入导出接口 ====================

    /**
     * 导出Excel
     */
    @Log(title = "库位管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @RequiresPermissions("wms:location:export")
    @Operation(summary = "导出库位数据", description = "根据查询条件导出Excel")
    public void export(HttpServletResponse response, @RequestBody WmsLocationRequest request) {
        log.info("导出库位数据: {}", request);
        TableDataInfo<WmsLocationResponse> tableData = wmsLocationBiz.list(request);
        ExcelUtil<WmsLocationResponse> util = new ExcelUtil<>(WmsLocationResponse.class);
        util.exportExcel(response, tableData.getRows(), i18nService.getMessage("export.filename.location"));
    }

    /**
     * 导入Excel（新增）
     */
    @Log(title = "库位管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    @RequiresPermissions("wms:location:import")
    @Operation(summary = "导入库位数据", description = "从Excel导入库位档案")
    public R<Boolean> importData(MultipartFile file) throws Exception {
        ExcelUtil<WmsLocationRequest> util = new ExcelUtil<>(WmsLocationRequest.class);
        List<WmsLocationRequest> list = util.importExcel(file.getInputStream());
        WmsLocationBatchRequest batchRequest = new WmsLocationBatchRequest();
        batchRequest.setRecords(list);
        return wmsLocationBiz.addBatch(batchRequest);
    }

    /**
     * 下载导入模板
     */
    @PostMapping("/downloadTemplate")
    @Operation(summary = "下载导入模板", description = "下载库位导入Excel模板")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil<WmsLocationRequest> util = new ExcelUtil<>(WmsLocationRequest.class);
        util.importTemplateExcel(response, i18nService.getMessage("export.filename.location"));
    }
}
