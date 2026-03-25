package com.abclonal.product.web.controller.system;

import com.abclonal.product.common.domain.R;
import com.abclonal.product.common.utils.poi.ExcelUtil;
import com.abclonal.product.common.web.controller.BaseController;
import com.abclonal.product.common.web.page.TableDataInfo;
import com.abclonal.product.common.log.enums.BusinessType;
import com.abclonal.product.dao.entity.SysDictType;
import com.abclonal.product.service.annotation.Log;
import com.abclonal.product.service.security.utils.SecurityUtils;
import com.abclonal.product.service.system.ISysDictTypeService;
import com.abclonal.product.service.system.service.I18nService;
import com.abclonal.product.web.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * 数据字典信息
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/api/dict/type")
public class SysDictTypeController extends BaseController
{
    @Autowired
    private ISysDictTypeService dictTypeService;

    @Autowired
    private I18nService i18nService;

    @Autowired
    private com.abclonal.product.biz.system.SysDictTypeBiz dictTypeBiz;

    @RequiresPermissions("system:dict:list")
    @GetMapping("/list")
    public R<TableDataInfo> list(SysDictType dictType)
    {
        startPage();
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        return R.ok(getDataTable(list));
    }

    @Log(title = "字典类型", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:dict:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody SysDictType dictType)
    {
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        ExcelUtil<SysDictType> util = new ExcelUtil<SysDictType>(SysDictType.class);
        util.exportExcel(response, list, i18nService.getMessage("export.filename.dict.type"));
    }

    /**
     * 查询字典类型详细
     */
    @RequiresPermissions("system:dict:query")
    @GetMapping(value = "/{dictId}")
    public R<SysDictType> getInfo(@PathVariable Long dictId)
    {
        return R.ok(dictTypeService.selectDictTypeById(dictId));
    }

    /**
     * 新增字典类型
     */
    @RequiresPermissions("system:dict:add")
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@Validated @RequestBody SysDictType dict)
    {
        if (!dictTypeService.checkDictTypeUnique(dict))
        {
            return R.fail("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setCreateBy(SecurityUtils.getUsername());
        return R.ok(dictTypeService.insertDictType(dict) > 0 ? "新增成功" : "新增失败");
    }

    /**
     * 修改字典类型
     */
    @RequiresPermissions("system:dict:edit")
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<String> edit(@Validated @RequestBody SysDictType dict)
    {
        if (!dictTypeService.checkDictTypeUnique(dict))
        {
            return R.fail("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setUpdateBy(SecurityUtils.getUsername());
        return R.ok(dictTypeService.updateDictType(dict) > 0 ? "修改成功" : "修改失败");
    }

    /**
     * 删除字典类型
     */
    @RequiresPermissions("system:dict:delete")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictIds}")
    public R<String> remove(@PathVariable Long[] dictIds)
    {
        String username = SecurityUtils.getUsername();
        dictTypeService.deleteDictTypeByIds(dictIds, username);
        return R.ok("删除成功");
    }

    /**
     * 刷新字典缓存
     */
    @RequiresPermissions("system:dict:refresh")
    @Log(title = "字典类型", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public R<String> refreshCache()
    {
        dictTypeService.resetDictCache();
        return R.ok("刷新成功");
    }

    /**
     * 修改字典类型状态
     */
    @RequiresPermissions("system:dict:edit")
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public R<String> changeStatus(@RequestBody SysDictType dict)
    {
        String username = SecurityUtils.getUsername();
        dict.setUpdateBy(username);
        return R.ok(dictTypeService.updateDictTypeStatus(dict) > 0 ? "修改成功" : "修改失败");
    }
    @GetMapping("/optionselect")
    public R<List<SysDictType>> optionselect()
    {
        List<SysDictType> dictTypes = dictTypeService.selectDictTypeAll();
        return R.ok(dictTypes);
    }

    /**
     * 导入字典类型数据
     */
    @RequiresPermissions("system:dict:import")
    @Log(title = "字典类型", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public R<String> importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysDictType> util = new ExcelUtil<SysDictType>(SysDictType.class);
        List<SysDictType> dictList = util.importExcel(file.getInputStream());
        String operName = SecurityUtils.getUsername();
        String message = dictTypeBiz.importDictType(dictList, updateSupport, operName);
        return R.ok(message);
    }

    /**
     * 下载导入模板
     */
    @RequiresPermissions("system:dict:import")
    @PostMapping("/downLoadTemplate")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil<SysDictType> util = new ExcelUtil<SysDictType>(SysDictType.class);
        util.importTemplateExcel(response, i18nService.getMessage("export.filename.dict.type"));
    }
}
