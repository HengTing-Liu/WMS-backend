package com.abclonal.product.web.controller.sys;

import com.abclonal.product.common.domain.R;
import com.abclonal.product.common.web.controller.BaseController;
import com.abclonal.product.common.web.page.TableDataInfo;
import com.abclonal.product.dao.entity.Material;
import com.abclonal.product.service.sys.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sys/material")
public class MaterialController extends BaseController {

    @Autowired
    private MaterialService materialService;

    @GetMapping("/page")
    public R<TableDataInfo> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        startPage();
        Material condition = new Material();
        List<Material> list;
        if (keyword != null && !keyword.trim().isEmpty()) {
            list = materialService.listByKeyword(keyword);
        } else {
            if (status != null) {
                condition.setStatus(status);
            }
            list = materialService.list(condition);
        }
        return R.ok(getDataTable(list));
    }

    @GetMapping("/{id}")
    public R<Material> getById(@PathVariable Long id) {
        return R.ok(materialService.getById(id));
    }

    @PostMapping
    public R<Long> create(@RequestBody Material material) {
        return R.ok(materialService.create(material));
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody Material material) {
        materialService.update(id, material);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        materialService.delete(id);
        return R.ok();
    }
}
