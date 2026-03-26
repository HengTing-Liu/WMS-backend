package com.abclonal.product.web.controller.sys;

import com.abclonal.product.common.domain.R;
import com.abclonal.product.common.web.controller.BaseController;
import com.abclonal.product.common.web.page.TableDataInfo;
import com.abclonal.product.dao.entity.Supplier;
import com.abclonal.product.service.sys.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/base/supplier")
public class SupplierController extends BaseController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping("/page")
    public R<TableDataInfo> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        startPage();
        Supplier condition = new Supplier();
        List<Supplier> list;
        if (keyword != null && !keyword.trim().isEmpty()) {
            list = supplierService.listByKeyword(keyword);
        } else {
            if (status != null) {
                condition.setStatus(status);
            }
            list = supplierService.list(condition);
        }
        return R.ok(getDataTable(list));
    }

    @GetMapping("/{id}")
    public R<Supplier> getById(@PathVariable Long id) {
        return R.ok(supplierService.getById(id));
    }

    @PostMapping
    public R<Long> create(@RequestBody Supplier supplier) {
        return R.ok(supplierService.create(supplier));
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody Supplier supplier) {
        supplierService.update(id, supplier);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        supplierService.delete(id);
        return R.ok();
    }
}
