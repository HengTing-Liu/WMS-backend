package com.abclonal.product.web.controller.system;

import com.abclonal.product.common.domain.R;
import com.abclonal.product.common.utils.poi.ExcelUtil;
import com.abclonal.product.common.web.controller.BaseController;
import com.abclonal.product.common.web.page.TableDataInfo;
import com.abclonal.product.common.log.enums.BusinessType;
import com.abclonal.product.dao.entity.SysConfig;
import com.abclonal.product.service.annotation.Log;
import com.abclonal.product.service.security.utils.SecurityUtils;
import com.abclonal.product.service.system.ISysConfigService;
import com.abclonal.product.service.system.service.I18nService;
import com.abclonal.product.web.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 参数配置 信息操作处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/api/config")
public class SysConfigController extends BaseController
{
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private I18nService i18nService;

    /**
     * 获取参数配置列表
     */
    @RequiresPermissions("system:config:list")
    @GetMapping("/list")
    public R<TableDataInfo> list(SysConfig config)
    {
        startPage();
        List<SysConfig> list = configService.selectConfigList(config);
        return R.ok(getDataTable(list));
    }

    @Log(title = "参数管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:config:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysConfig config)
    {
        List<SysConfig> list = configService.selectConfigList(config);
        ExcelUtil<SysConfig> util = new ExcelUtil<SysConfig>(SysConfig.class);
        util.exportExcel(response, list, i18nService.getMessage("export.filename.config"));
    }

    /**
     * 根据参数编号获取详细信息
     */
    @GetMapping(value = "/{configId}")
    public R<SysConfig> getInfo(@PathVariable Long configId)
    {
        return R.ok(configService.selectConfigById(configId));
    }

    /**
     * 根据参数键名查询参数值
     */
    @GetMapping(value = "/configKey/{configKey}")
    public R<String> getConfigKey(@PathVariable String configKey)
    {
        return R.ok(configService.selectConfigByKey(configKey));
    }

    /**
     * 新增参数配置
     */
    @RequiresPermissions("system:config:add")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@Validated @RequestBody SysConfig config)
    {
        if (!configService.checkConfigKeyUnique(config))
        {
            return R.fail("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setCreateBy(SecurityUtils.getUsername());
        return R.ok(configService.insertConfig(config) > 0 ? "新增成功" : "新增失败");
    }

    /**
     * 修改参数配置
     */
    @RequiresPermissions("system:config:edit")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<String> edit(@Validated @RequestBody SysConfig config)
    {
        if (!configService.checkConfigKeyUnique(config))
        {
            return R.fail("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setUpdateBy(SecurityUtils.getUsername());
        return R.ok(configService.updateConfig(config) > 0 ? "修改成功" : "修改失败");
    }

    /**
     * 删除参数配置
     */
    @RequiresPermissions("system:config:remove")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    public R<String> remove(@PathVariable Long[] configIds)
    {
        configService.deleteConfigByIds(configIds);
        return R.ok("删除成功");
    }

    /**
     * 刷新参数缓存
     */
    @RequiresPermissions("system:config:remove")
    @Log(title = "参数管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public R<String> refreshCache()
    {
        configService.resetConfigCache();
        return R.ok("刷新成功");
    }
}
