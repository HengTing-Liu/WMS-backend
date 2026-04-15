package com.abtk.product.web.controller.system;

import com.abtk.product.common.constant.UserConstants;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.utils.StringUtils;
import com.abtk.product.common.web.controller.BaseController;
import com.abtk.product.common.web.page.TableDataInfo;
import com.abtk.product.common.log.enums.BusinessType;
import com.abtk.product.dao.entity.SysMenu;
import com.abtk.product.service.annotation.Log;
import com.abtk.product.service.security.utils.SecurityUtils;
import com.abtk.product.service.system.ISysMenuService;
import com.abtk.product.web.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单信息
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/api/menu")
public class SysMenuController extends BaseController
{
    @Autowired
    private ISysMenuService menuService;

    /**
     * 获取菜单列表
     */
    @RequiresPermissions("system:menu:list")
    @GetMapping("/list")
    public R<TableDataInfo> list(SysMenu menu)
    {
        // 菜单管理不分页，返回全部数据
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        return R.ok(getDataTable(menus));
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @RequiresPermissions("system:menu:query")
    @GetMapping(value = "/{menuId}")
    public R<SysMenu> getInfo(@PathVariable Long menuId)
    {
        return R.ok(menuService.selectMenuById(menuId));
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeselect")
    public R<List> treeselect(SysMenu menu)
    {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        return R.ok(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public R<Map<String, Object>> roleMenuTreeselect(@PathVariable("roleId") Long roleId)
    {
        Long userId = SecurityUtils.getUserId();
        SysMenu menu = new SysMenu();
        menu.setMenuType("ALL"); // 跳过默认的 M/C 过滤，返回包含按钮(F)的完整菜单树
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        Map<String, Object> result = new HashMap<>();
        result.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        result.put("menus", menuService.buildMenuTreeSelect(menus));
        return R.ok(result);
    }

    /**
     * 新增菜单
     */
    @RequiresPermissions("system:menu:add")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@Validated @RequestBody SysMenu menu)
    {
        if (!menuService.checkMenuNameUnique(menu))
        {
            return R.fail("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath()))
        {
            return R.fail("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        menu.setCreateBy(SecurityUtils.getUsername());
        return R.ok(menuService.insertMenu(menu) > 0 ? "新增成功" : "新增失败");
    }

    /**
     * 修改菜单
     */
    @RequiresPermissions("system:menu:edit")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<String> edit(@Validated @RequestBody SysMenu menu)
    {
        if (!menuService.checkMenuNameUnique(menu))
        {
            return R.fail("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath()))
        {
            return R.fail("修改菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        else if (menu.getMenuId().equals(menu.getParentId()))
        {
            return R.fail("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menu.setUpdateBy(SecurityUtils.getUsername());
        return R.ok(menuService.updateMenu(menu) > 0 ? "修改成功" : "修改失败");
    }

    /**
     * 修改菜单状态
     */
    @RequiresPermissions("system:menu:edit")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public R<String> changeStatus(@RequestBody SysMenu menu)
    {
        String username = SecurityUtils.getUsername();
        menu.setUpdateBy(username);
        return R.ok(menuService.updateMenuStatus(menu) > 0 ? "修改成功" : "修改失败");
    }

    /**
     * 修改菜单显示状态
     */
    @RequiresPermissions("system:menu:edit")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeVisible")
    public R<String> changeVisible(@RequestBody SysMenu menu)
    {
        String username = SecurityUtils.getUsername();
        menu.setUpdateBy(username);
        return R.ok(menuService.updateMenuVisible(menu) > 0 ? "修改成功" : "修改失败");
    }

    /**
     * 删除菜单
     */
    @RequiresPermissions("system:menu:delete")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    public R<String> remove(@PathVariable("menuId") Long menuId)
    {
        if (menuService.hasChildByMenuId(menuId))
        {
            return R.fail("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId))
        {
            return R.fail("菜单已分配,不允许删除");
        }
        return R.ok(menuService.deleteMenuById(menuId) > 0 ? "删除成功" : "删除失败");
    }

    /**
     * 获取路由信息
     * 
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public R<List> getRouters()
    {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return R.ok(menuService.buildMenus(menus));
    }

    @GetMapping("getRouters2")
    public R<?> getRouters2()
    {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return R.ok(menuService.buildMenus2(menus));
    }
}
