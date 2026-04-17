package com.abtk.product.web.controller.sys;

import com.abtk.product.common.domain.R;
import com.abtk.product.common.web.controller.BaseController;
import com.abtk.product.common.web.page.TableDataInfo;
import com.abtk.product.dao.entity.Batch;
import com.abtk.product.service.sys.service.BatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 批次档案 Controller
 * 用于物料批次查询（WMS0030）
 */
@Tag(name = "批次档案管理", description = "批次档案相关接口")
@RestController
@RequestMapping("/api/wms/batch")
public class BatchController extends BaseController {

    @Autowired
    private BatchService batchService;

    /**
     * 物料批次分页列表查询
     * 支持按物料编码、名称、批次号、仓库、质检状态等条件组合查询
     */
    @GetMapping("/list")
    @Operation(summary = "物料批次分页列表", description = "支持按物料编码、名称、批次号、仓库、质检状态等条件组合查询")
    public R<TableDataInfo> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) String materialCode,
            @RequestParam(required = false) String materialName,
            @RequestParam(required = false) String batchNo,
            @RequestParam(required = false) String warehouseCode,
            @RequestParam(required = false) String qcStatus) {
        startPage();
        List<Batch> list = batchService.listMaterialBatch(materialCode, materialName, batchNo,
                                                           warehouseCode, qcStatus);
        return R.ok(getDataTable(list));
    }

    /**
     * 查询所有批次（不分页，用于下拉选择）
     */
    @GetMapping("/all")
    @Operation(summary = "查询所有批次", description = "查询所有批次不分页，用于下拉选择")
    public R<List<Batch>> listAll() {
        return R.ok(batchService.listAll());
    }

    /**
     * 根据物料ID查询批次列表
     */
    @GetMapping("/material/{materialId}")
    @Operation(summary = "根据物料ID查询批次", description = "根据物料ID查询批次列表")
    public R<List<Batch>> listByMaterialId(
            @Parameter(description = "物料ID") @PathVariable Long materialId) {
        return R.ok(batchService.listByMaterialId(materialId));
    }

    /**
     * 查询批次详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询批次详情", description = "根据ID查询批次详细信息")
    @Parameter(name = "id", description = "批次ID", required = true)
    public R<Batch> getById(@PathVariable Long id) {
        return R.ok(batchService.getById(id));
    }

    /**
     * 新增批次
     */
    @PostMapping
    @Operation(summary = "新增批次", description = "新增批次档案")
    public R<Long> create(@RequestBody Batch batch) {
        return R.ok(batchService.create(batch));
    }

    /**
     * 更新批次
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新批次", description = "更新批次档案信息")
    @Parameter(name = "id", description = "批次ID", required = true)
    public R<Void> update(@PathVariable Long id, @RequestBody Batch batch) {
        batchService.update(id, batch);
        return R.ok();
    }

    /**
     * 删除批次（逻辑删除）
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除批次", description = "逻辑删除批次档案")
    @Parameter(name = "id", description = "批次ID", required = true)
    public R<Void> delete(@PathVariable Long id) {
        batchService.delete(id);
        return R.ok();
    }

    /**
     * 导出批次档案
     */
    @PostMapping("/export")
    @Operation(summary = "导出批次档案", description = "导出批次档案 Excel")
    public void export(HttpServletResponse response,
            @RequestParam(required = false) String materialCode,
            @RequestParam(required = false) String materialName,
            @RequestParam(required = false) String batchNo,
            @RequestParam(required = false) String qcStatus) {
        batchService.export(response, materialCode, materialName, batchNo, qcStatus);
    }
}
