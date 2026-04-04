package com.abtk.product.web.controller.system;

import com.abtk.product.common.constant.UserConstants;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.utils.StringUtils;
import com.abtk.product.common.web.controller.BaseController;
import com.abtk.product.common.web.page.TableDataInfo;
import com.abtk.product.common.log.enums.BusinessType;
import com.abtk.product.dao.entity.SysDept;
import com.abtk.product.service.annotation.Log;
import com.abtk.product.service.security.utils.SecurityUtils;
import com.abtk.product.service.system.ISysDeptService;
import com.abtk.product.web.security.annotation.RequiresPermissions;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门信息
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/api/dept")
public class SysDeptController extends BaseController
{
    @Autowired
    private ISysDeptService deptService;

    /**
     * 获取部门下拉列表（用于表单下拉选择）
     */
    @GetMapping("/options")
    public R<List<SysDept>> options()
    {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        return R.ok(depts);
    }

    /**
     * 获取部门列表
     */
    @RequiresPermissions("system:dept:list")
    @GetMapping("/list")
    public R<TableDataInfo> list(SysDept dept)
    {
        startPage();
        List<SysDept> depts = deptService.selectDeptList(dept);
        return R.ok(getDataTable(depts));
    }

    /**
     * 查询部门列表（排除节点）
     */
    @RequiresPermissions("system:dept:list")
    @GetMapping("/list/exclude/{deptId}")
    public R<List<SysDept>> excludeChild(@PathVariable(value = "deptId", required = false) Long deptId)
    {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        depts.removeIf(d -> d.getDeptId().intValue() == deptId || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + ""));
        return R.ok(depts);
    }

    /**
     * 根据部门编号获取详细信息
     */
    @RequiresPermissions("system:dept:query")
    @GetMapping(value = "/{deptId}")
    public R<SysDept> getInfo(@PathVariable Long deptId)
    {
        deptService.checkDeptDataScope(deptId);
        return R.ok(deptService.selectDeptById(deptId));
    }

    /**
     * 新增部门
     */
    @RequiresPermissions("system:dept:add")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@Validated @RequestBody SysDept dept)
    {
        if (!deptService.checkDeptNameUnique(dept))
        {
            return R.fail("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        dept.setCreateBy(SecurityUtils.getUsername());
        return R.ok(deptService.insertDept(dept) > 0 ? "新增成功" : "新增失败");
    }

    /**
     * 修改部门
     */
    @RequiresPermissions("system:dept:edit")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<String> edit(@Validated @RequestBody SysDept dept)
    {
        Long deptId = dept.getDeptId();
        deptService.checkDeptDataScope(deptId);
        if (!deptService.checkDeptNameUnique(dept))
        {
            return R.fail("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        else if (dept.getParentId().equals(deptId))
        {
            return R.fail("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        }
        else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus()) && deptService.selectNormalChildrenDeptById(deptId) > 0)
        {
            return R.fail("该部门包含未停用的子部门！");
        }
        dept.setUpdateBy(SecurityUtils.getUsername());
        return R.ok(deptService.updateDept(dept) > 0 ? "修改成功" : "修改失败");
    }

    /**
     * 修改部门状态
     */
    @RequiresPermissions("system:dept:edit")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public R<String> changeStatus(@RequestBody SysDept dept)
    {
        String username = SecurityUtils.getUsername();
        dept.setUpdateBy(username);
        return R.ok(deptService.updateDeptStatus(dept) > 0 ? "修改成功" : "修改失败");
    }

    /**
     * 删除部门
     */
    @RequiresPermissions("system:dept:delete")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public R<String> remove(@PathVariable Long deptId)
    {
        if (deptService.hasChildByDeptId(deptId))
        {
            return R.fail("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId))
        {
            return R.fail("部门存在用户,不允许删除");
        }
        deptService.checkDeptDataScope(deptId);
        return R.ok(deptService.deleteDeptById(deptId) > 0 ? "删除成功" : "删除失败");
    }
}
