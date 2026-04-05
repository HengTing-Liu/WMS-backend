package com.abtk.product.web.controller.sys;

import com.abtk.product.common.domain.R;
import com.abtk.product.common.web.controller.BaseController;
import com.abtk.product.common.web.page.TableDataInfo;
import com.abtk.product.dao.entity.Material;
import com.abtk.product.service.sys.service.MaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Tag(name = "物料管理", description = "物料档案相关接口")
@RestController
@RequestMapping("/api/base/material")
public class MaterialController extends BaseController {

    @Autowired
    private MaterialService materialService;

    @GetMapping("/list")
    @Operation(summary = "物料分页列表", description = "支持按物料编码、名称、分类、状态、UDI标识筛选")
    @Parameter(name = "page", description = "页码", required = false)
    @Parameter(name = "size", description = "每页条数", required = false)
    public R<TableDataInfo> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String materialCode,
            @RequestParam(required = false) String materialName,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Boolean udiFlag) {
        startPage();
        Material condition = new Material();
        condition.setMaterialCode(materialCode);
        condition.setMaterialName(materialName);
        condition.setCategory(category);
        condition.setStatus(status);
        List<Material> list = materialService.list(condition);
        return R.ok(getDataTable(list));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询物料详情", description = "根据ID查询物料详细信息")
    @Parameter(name = "id", description = "物料ID", required = true)
    public R<Material> getById(@PathVariable Long id) {
        return R.ok(materialService.getById(id));
    }

    @PostMapping
    @Operation(summary = "新增物料", description = "新增物料档案")
    public R<Long> create(@RequestBody Material material) {
        return R.ok(materialService.create(material));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新物料", description = "更新物料档案信息")
    @Parameter(name = "id", description = "物料ID", required = true)
    public R<Void> update(@PathVariable Long id, @RequestBody Material material) {
        materialService.update(id, material);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除物料", description = "逻辑删除物料档案")
    @Parameter(name = "id", description = "物料ID", required = true)
    public R<Void> delete(@PathVariable Long id) {
        materialService.delete(id);
        return R.ok();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "切换物料状态", description = "启用或禁用物料")
    @Parameter(name = "id", description = "物料ID", required = true)
    public R<Void> toggleStatus(@PathVariable Long id, @RequestParam Integer enabled) {
        materialService.toggleStatus(id, enabled);
        return R.ok();
    }

    @PostMapping("/export")
    @Operation(summary = "导出物料", description = "导出物料档案 Excel")
    public void export(HttpServletResponse response,
            @RequestParam(required = false) String materialCode,
            @RequestParam(required = false) String materialName,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer status) {
        materialService.export(response, materialCode, materialName, category, status);
    }
}
