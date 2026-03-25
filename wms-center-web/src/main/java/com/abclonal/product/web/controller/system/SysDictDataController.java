package com.abclonal.product.web.controller.system;

import com.abclonal.product.common.domain.R;
import com.abclonal.product.common.utils.StringUtils;
import com.abclonal.product.common.utils.poi.ExcelUtil;
import com.abclonal.product.common.web.controller.BaseController;
import com.abclonal.product.common.web.page.TableDataInfo;
import com.abclonal.product.common.log.enums.BusinessType;
import com.abclonal.product.dao.entity.SysDictData;
import com.abclonal.product.service.annotation.Log;
import com.abclonal.product.service.security.utils.SecurityUtils;
import com.abclonal.product.service.system.ISysDictDataService;
import com.abclonal.product.service.system.service.I18nService;
import com.abclonal.product.service.system.ISysDictTypeService;
import com.abclonal.product.web.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典信息
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/api/dict/data")
public class SysDictDataController extends BaseController
{
    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private I18nService i18nService;
    
    @Autowired
    private ISysDictTypeService dictTypeService;


    /**
     * 查询字典数据列表
     *
     * @param dictData 字典数据查询条件
     * @return 字典数据分页结果
     */
    //@RequiresPermissions("system:dict:list")
    @GetMapping("/list")
    public R<TableDataInfo> list(SysDictData dictData)
    {
        startPage();
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return R.ok(getDataTable(list));
    }

    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
    // @RequiresPermissions("system:dict:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDictData dictData)
    {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
        util.exportExcel(response, list, "字典数据");
    }

    /**
     * 查询字典数据详细
     */
    // @RequiresPermissions("system:dict:query")
    @GetMapping(value = "/{dictCode}")
    public R<SysDictData> getInfo(@PathVariable Long dictCode)
    {
        return R.ok(dictDataService.selectDictDataById(dictCode));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    public R<List<SysDictData>> dictType(@PathVariable String dictType)
    {
        List<SysDictData> data = dictTypeService.selectDictDataByType(dictType);
        if (StringUtils.isNull(data))
        {
            data = new ArrayList<SysDictData>();
        }
        return R.ok(data);
    }

    /**
     * 新增字典类型
     */
    // @RequiresPermissions("system:dict:add")
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@Validated @RequestBody SysDictData dict)
    {
        dict.setCreateBy(SecurityUtils.getUsername());
        return R.ok(dictDataService.insertDictData(dict) > 0 ? "新增成功" : "新增失败");
    }

    /**
     * 修改保存字典类型
     */
    // @RequiresPermissions("system:dict:edit")
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<String> edit(@Validated @RequestBody SysDictData dict)
    {
        dict.setUpdateBy(SecurityUtils.getUsername());
        return R.ok(dictDataService.updateDictData(dict) > 0 ? "修改成功" : "修改失败");
    }

    /**
     * 删除字典类型
     */
    // @RequiresPermissions("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictCodes}")
    public R<String> remove(@PathVariable Long[] dictCodes)
    {
        dictDataService.deleteDictDataByIds(dictCodes);
        return R.ok("删除成功");
    }
}
