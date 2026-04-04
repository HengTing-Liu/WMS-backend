package com.abtk.product.web.controller.sys;

import com.abtk.product.api.domain.request.sys.CustomerQueryRequest;
import com.abtk.product.api.domain.request.sys.CustomerRequest;
import com.abtk.product.biz.sys.CustomerBiz;
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
 * 客户档案Controller
 */
@RestController
@RequestMapping("/api/base/customer")
public class CustomerController extends BaseController {

    @Autowired
    private CustomerBiz customerBiz;

    /**
     * 分页查询客户列表
     */
    @RequiresPermissions("wms:base:customer:list")
    @GetMapping("/list")
    public R<TableDataInfo> list(CustomerQueryRequest queryRequest) {
        startPage();
        return R.ok(getDataTable(customerBiz.list(queryRequest).getData()));
    }

    /**
     * 查询所有客户（不分页）
     */
    @RequiresPermissions("wms:base:customer:list")
    @GetMapping("/listAll")
    public R listAll() {
        return customerBiz.listAll();
    }

    /**
     * 根据ID查询客户详情
     */
    @RequiresPermissions("wms:base:customer:list")
    @GetMapping("/{id}")
    public R getById(@PathVariable Long id) {
        return customerBiz.queryById(id);
    }

    /**
     * 新增客户
     */
    @Log(title = "客户档案", businessType = BusinessType.INSERT)
    @RequiresPermissions("wms:base:customer:add")
    @PostMapping
    public R create(@RequestBody @Valid CustomerRequest request) {
        return customerBiz.add(request);
    }

    /**
     * 编辑客户
     */
    @Log(title = "客户档案", businessType = BusinessType.UPDATE)
    @RequiresPermissions("wms:base:customer:edit")
    @PutMapping("/{id}")
    public R update(@PathVariable Long id, @RequestBody @Valid CustomerRequest request) {
        return customerBiz.update(id, request);
    }

    /**
     * 删除客户
     */
    @Log(title = "客户档案", businessType = BusinessType.DELETE)
    @RequiresPermissions("wms:base:customer:delete")
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        return customerBiz.delete(id);
    }

    /**
     * 切换客户状态
     */
    @Log(title = "客户档案", businessType = BusinessType.UPDATE)
    @RequiresPermissions("wms:base:customer:edit")
    @PatchMapping("/{id}/status")
    public R toggleStatus(@PathVariable Long id, @RequestParam Integer enabled) {
        return customerBiz.toggleStatus(id, enabled);
    }
}
