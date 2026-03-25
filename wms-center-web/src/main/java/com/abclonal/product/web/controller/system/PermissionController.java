package com.abclonal.product.web.controller.system;

import com.abclonal.product.common.domain.R;
import com.abclonal.product.common.utils.StringUtils;
import com.abclonal.product.common.web.controller.BaseController;
import com.abclonal.product.common.log.enums.BusinessType;
import com.abclonal.product.dao.entity.SysMenu;
import com.abclonal.product.dao.entity.SysMenuMetaMap;
import com.abclonal.product.dao.mapper.SysMenuMetaMapMapper;
import com.abclonal.product.service.annotation.Log;
import com.abclonal.product.service.security.utils.SecurityUtils;
import com.abclonal.product.service.system.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限点管理（低代码驱动）
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/api/permission")
public class PermissionController extends BaseController
{
    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysMenuMetaMapMapper menuMetaMapMapper;

    /**
     * 获取权限树（menu_type IN ('C','M','F')）
     */
    @GetMapping("/tree")
    public R<List<SysMenu>> permissionTree()
    {
        List<SysMenu> tree = menuService.selectPermissionTree();
        return R.ok(tree);
    }

    /**
     * 获取低代码配置映射
     */
    @GetMapping("/menu-map")
    public R<List<SysMenuMetaMap>> menuMetaMap()
    {
        List<SysMenuMetaMap> list = menuMetaMapMapper.selectMenuMetaMapList();
        return R.ok(list);
    }

    /**
     * 获取低代码配置映射（单个菜单）
     */
    @GetMapping("/menu-map/{menuId}")
    public R<SysMenuMetaMap> menuMetaMapById(@PathVariable("menuId") Long menuId)
    {
        return R.ok(menuMetaMapMapper.selectMenuMetaMapById(menuId));
    }

    /**
     * 根据菜单ID获取权限点详情
     */
    @GetMapping("/{menuId}")
    public R<Map<String, Object>> getInfo(@PathVariable Long menuId)
    {
        SysMenu menu = menuService.selectMenuById(menuId);
        if (menu == null)
        {
            return R.fail("菜单不存在");
        }
        // 同时返回低代码配置
        SysMenuMetaMap metaMap = menuMetaMapMapper.selectMenuMetaMapById(menuId);
        Map<String, Object> result = new HashMap<>();
        result.put("menu", menu);
        result.put("metaMap", metaMap);
        return R.ok(result);
    }

    /**
     * 新增权限点
     */
    @Log(title = "权限点管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@Validated @RequestBody SysMenu menu)
    {
        if (!menuService.checkMenuNameUnique(menu))
        {
            return R.fail("新增权限点'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        // 仅F类按钮需要perms唯一性校验
        if ("F".equals(menu.getMenuType()))
        {
            if (!menuService.checkPermsUnique(menu))
            {
                return R.fail("权限码已存在");
            }
        }
        menu.setCreateBy(SecurityUtils.getUsername());
        int rows = menuService.insertMenu(menu);
        if (rows > 0)
        {
            // 同步插入低代码映射（如果传了entityName）
            SysMenuMetaMap metaMap = buildMetaMapFromMenu(menu);
            if (metaMap != null)
            {
                menuMetaMapMapper.insertMenuMetaMap(metaMap);
            }
            return R.ok("新增成功");
        }
        return R.fail("新增失败");
    }

    /**
     * 更新权限点
     */
    @Log(title = "权限点管理", businessType = BusinessType.UPDATE)
    @PutMapping("/{menuId}")
    public R<String> edit(@PathVariable("menuId") Long menuId, @Validated @RequestBody SysMenu menu)
    {
        if (menuId == null)
        {
            return R.fail("菜单ID不能为空");
        }
        menu.setMenuId(menuId);
        if (!menuService.checkMenuNameUnique(menu))
        {
            return R.fail("修改权限点'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        // 仅F类按钮需要perms唯一性校验
        if ("F".equals(menu.getMenuType()))
        {
            if (!menuService.checkPermsUnique(menu))
            {
                return R.fail("权限码已存在");
            }
        }
        menu.setUpdateBy(SecurityUtils.getUsername());
        int rows = menuService.updateMenu(menu);
        if (rows > 0)
        {
            // 同步更新低代码映射
            SysMenuMetaMap metaMap = buildMetaMapFromMenu(menu);
            if (metaMap != null)
            {
                SysMenuMetaMap existing = menuMetaMapMapper.selectMenuMetaMapById(menuId);
                if (existing != null)
                {
                    menuMetaMapMapper.updateMenuMetaMap(metaMap);
                }
                else
                {
                    menuMetaMapMapper.insertMenuMetaMap(metaMap);
                }
            }
            return R.ok("修改成功");
        }
        return R.fail("修改失败");
    }

    /**
     * 删除权限点（仅F类按钮允许删除）
     */
    @Log(title = "权限点管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    public R<String> remove(@PathVariable("menuId") Long menuId)
    {
        SysMenu menu = menuService.selectMenuById(menuId);
        if (menu == null)
        {
            return R.fail("菜单不存在");
        }
        // 仅允许删除F类（按钮）权限点
        if (!"F".equals(menu.getMenuType()))
        {
            return R.fail("仅允许删除按钮权限点（F类）");
        }
        if (menuService.hasChildByMenuId(menuId))
        {
            return R.fail("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId))
        {
            return R.fail("菜单已分配,不允许删除");
        }
        // 先删除低代码映射
        menuMetaMapMapper.deleteMenuMetaMapById(menuId);
        int rows = menuService.deleteMenuById(menuId);
        if (rows > 0)
        {
            return R.ok("删除成功");
        }
        return R.fail("删除失败");
    }

    /**
     * 从菜单对象构建低代码映射（如果entityName存在）
     */
    private SysMenuMetaMap buildMetaMapFromMenu(SysMenu menu)
    {
        // 从请求参数中获取低代码配置（如果前端传了的话）
        Map<String, Object> params = menu.getParams();
        if (params == null)
        {
            return null;
        }
        String entityName = params.get("entityName") != null ? params.get("entityName").toString() : null;
        if (StringUtils.isEmpty(entityName))
        {
            return null;
        }
        SysMenuMetaMap metaMap = new SysMenuMetaMap();
        metaMap.setMenuId(menu.getMenuId());
        metaMap.setEntityName(entityName);
        if (params.get("listMode") != null)
        {
            metaMap.setListMode(params.get("listMode").toString());
        }
        else
        {
            metaMap.setListMode("tree");
        }
        if (params.get("treeLabelField") != null)
        {
            metaMap.setTreeLabelField(params.get("treeLabelField").toString());
        }
        else
        {
            metaMap.setTreeLabelField("menuName");
        }
        if (params.get("treeParentField") != null)
        {
            metaMap.setTreeParentField(params.get("treeParentField").toString());
        }
        else
        {
            metaMap.setTreeParentField("parentId");
        }
        metaMap.setTreeNodeType(menu.getMenuType());
        return metaMap;
    }
}
