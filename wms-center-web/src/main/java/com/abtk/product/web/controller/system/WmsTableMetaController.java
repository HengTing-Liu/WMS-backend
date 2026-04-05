package com.abtk.product.web.controller.system;

import com.abtk.product.common.domain.R;
import com.abtk.product.common.log.enums.BusinessType;
import com.abtk.product.common.web.controller.BaseController;
import com.abtk.product.common.web.page.TableDataInfo;
import com.abtk.product.dao.entity.WmsTableMeta;
import com.abtk.product.service.annotation.Log;
import com.abtk.product.service.security.utils.SecurityUtils;
import com.abtk.product.service.system.service.IWmsTableMetaService;
import com.abtk.product.web.security.annotation.RequiresPermissions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 表元数据管理(WmsTableMeta)控制层
 *
 * @author backend
 * @since 2026-04-06
 */
@Slf4j
@RestController
@RequestMapping("/api/system/meta/table")
@Tag(name = "表元数据管理", description = "表元数据管理相关接口")
public class WmsTableMetaController extends BaseController {

    @Autowired
    private IWmsTableMetaService wmsTableMetaService;

    /**
     * 获取表元数据列表（分页 + 模糊搜索）
     */
    @GetMapping
    @RequiresPermissions("system:meta:table:query")
    @Operation(summary = "获取表元数据列表", description = "支持分页查询和模糊搜索")
    public R<TableDataInfo<WmsTableMeta>> list(WmsTableMeta wmsTableMeta) {
        startPage();
        List<WmsTableMeta> list = wmsTableMetaService.queryByCondition(wmsTableMeta);
        return R.ok(getDataTable(list));
    }

    /**
     * 获取所有表元数据（不分页，供下拉框等场景使用）
     */
    @GetMapping("/all")
    @RequiresPermissions("system:meta:table:query")
    @Operation(summary = "获取所有表元数据", description = "不分页，用于下拉框等场景")
    public R<List<WmsTableMeta>> listAll() {
        return R.ok(wmsTableMetaService.listAll());
    }

    /**
     * 通过表编码查询
     */
    @GetMapping("/code/{code}")
    @RequiresPermissions("system:meta:table:query")
    @Operation(summary = "通过编码查询表元数据", description = "根据表编码精确查询")
    @Parameter(name = "code", description = "表编码", required = true)
    public R<WmsTableMeta> queryByCode(@PathVariable("code") String code) {
        WmsTableMeta result = wmsTableMetaService.queryByCode(code);
        if (result == null) {
            return R.fail("表编码不存在");
        }
        return R.ok(result);
    }

    /**
     * 获取表元数据详情
     */
    @GetMapping("/{id}")
    @RequiresPermissions("system:meta:table:query")
    @Operation(summary = "获取表元数据详情", description = "根据ID查询表元数据详情")
    @Parameter(name = "id", description = "表元数据ID", required = true)
    public R<WmsTableMeta> queryById(@PathVariable("id") Long id) {
        WmsTableMeta result = wmsTableMetaService.queryById(id);
        if (result == null) {
            return R.fail("表元数据不存在");
        }
        return R.ok(result);
    }

    /**
     * 新增表元数据
     */
    @PostMapping
    @RequiresPermissions("system:meta:table:manage")
    @Log(title = "表元数据", businessType = BusinessType.INSERT)
    @Operation(summary = "新增表元数据", description = "创建新的表元数据记录")
    public R<String> add(@Validated @RequestBody WmsTableMeta wmsTableMeta) {
        // 校验表编码唯一性
        if (!wmsTableMetaService.checkTableCodeUnique(wmsTableMeta.getTableCode())) {
            return R.fail("新增表元数据失败，表编码已存在");
        }
        wmsTableMeta.setCreateBy(SecurityUtils.getUsername());
        WmsTableMeta result = wmsTableMetaService.insert(wmsTableMeta);
        return R.ok("新增成功");
    }

    /**
     * 更新表元数据
     */
    @PutMapping("/{id}")
    @RequiresPermissions("system:meta:table:manage")
    @Log(title = "表元数据", businessType = BusinessType.UPDATE)
    @Operation(summary = "更新表元数据", description = "根据ID更新表元数据")
    @Parameter(name = "id", description = "表元数据ID", required = true)
    public R<String> update(@PathVariable("id") Long id, @Validated @RequestBody WmsTableMeta wmsTableMeta) {
        // 校验表编码唯一性（排除自身）
        WmsTableMeta existing = wmsTableMetaService.queryById(id);
        if (existing == null) {
            return R.fail("表元数据不存在");
        }
        // 如果表编码被修改了，需要校验新编码唯一性
        if (!existing.getTableCode().equals(wmsTableMeta.getTableCode())) {
            if (!wmsTableMetaService.checkTableCodeUnique(wmsTableMeta.getTableCode())) {
                return R.fail("更新表元数据失败，表编码已存在");
            }
        }
        wmsTableMeta.setId(id);
        wmsTableMeta.setUpdateBy(SecurityUtils.getUsername());
        int rows = wmsTableMetaService.update(wmsTableMeta);
        return rows > 0 ? R.ok("更新成功") : R.fail("更新失败");
    }

    /**
     * 删除表元数据（逻辑删除）
     */
    @DeleteMapping("/{id}")
    @RequiresPermissions("system:meta:table:manage")
    @Log(title = "表元数据", businessType = BusinessType.DELETE)
    @Operation(summary = "删除表元数据", description = "根据ID删除表元数据（逻辑删除）")
    @Parameter(name = "id", description = "表元数据ID", required = true)
    public R<String> deleteById(@PathVariable("id") Long id) {
        // 检查是否有关联字段
        if (wmsTableMetaService.hasRelatedFields(id)) {
            return R.fail("删除失败，该表存在关联字段，请先删除关联字段");
        }
        boolean result = wmsTableMetaService.logicDeleteById(id, SecurityUtils.getUsername());
        return result ? R.ok("删除成功") : R.fail("删除失败");
    }

    /**
     * 启用/禁用切换
     */
    @PutMapping("/{id}/toggle")
    @RequiresPermissions("system:meta:table:manage")
    @Log(title = "表元数据", businessType = BusinessType.UPDATE)
    @Operation(summary = "启用/禁用切换", description = "切换表元数据的启用状态")
    @Parameter(name = "id", description = "表元数据ID", required = true)
    public R<String> toggleStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        WmsTableMeta existing = wmsTableMetaService.queryById(id);
        if (existing == null) {
            return R.fail("表元数据不存在");
        }
        wmsTableMetaService.toggleStatus(id, status);
        return R.ok(status == 1 ? "启用成功" : "禁用成功");
    }
}
