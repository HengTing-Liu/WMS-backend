package com.abtk.product.web.controller.system;

import com.abtk.product.common.domain.R;
import com.abtk.product.common.utils.StringUtils;
import com.abtk.product.common.utils.poi.ExcelUtil;
import com.abtk.product.common.web.controller.BaseController;
import com.abtk.product.common.web.page.TableDataInfo;
import com.abtk.product.common.log.enums.BusinessType;
import com.abtk.product.dao.entity.SysDictData;
import com.abtk.product.service.annotation.Log;
import com.abtk.product.service.security.utils.SecurityUtils;
import com.abtk.product.biz.system.SysDictDataBiz;
import com.abtk.product.service.system.ISysDictDataService;
import com.abtk.product.service.system.ISysDictTypeService;
import com.abtk.product.service.system.service.I18nService;
import com.abtk.product.web.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    @Autowired
    private SysDictDataBiz dictDataBiz;


    /**
     * 查询字典数据列表
     */
    @GetMapping("/list")
    public R<TableDataInfo> list(SysDictData dictData)
    {
        startPage();
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return R.ok(getDataTable(list));
    }

    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
    @RequiresPermissions("wms:base:dict:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody SysDictData dictData)
    {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
        util.exportExcel(response, list, "字典数据");
    }

    /**
     * 导入字典数据
     */
    @RequiresPermissions("wms:base:dict:import")
    @Log(title = "字典数据", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public R<String> importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
        List<SysDictData> dictList = util.importExcel(file.getInputStream());
        String operName = SecurityUtils.getUsername();
        String message = dictDataBiz.importDictData(dictList, updateSupport, operName);
        return R.ok(message);
    }

    /**
     * 下载导入模板
     */
    @RequiresPermissions("wms:base:dict:import")
    @PostMapping("/downLoadTemplate")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
        util.importTemplateExcel(response, "字典数据");
    }

    /**
     * 查询字典数据详细
     */
    @GetMapping(value = "/{id}")
    public R<SysDictData> getInfo(@PathVariable Long id)
    {
        return R.ok(dictDataService.selectDictDataById(id));
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
     * 新增字典数据
     */
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@Validated @RequestBody SysDictData dict)
    {
        dict.setCreateBy(SecurityUtils.getUsername());
        return R.ok(dictDataService.insertDictData(dict) > 0 ? "新增成功" : "新增失败");
    }

    /**
     * 修改保存字典数据
     */
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<String> edit(@Validated @RequestBody SysDictData dict)
    {
        dict.setUpdateBy(SecurityUtils.getUsername());
        return R.ok(dictDataService.updateDictData(dict) > 0 ? "修改成功" : "修改失败");
    }

    /**
     * 删除字典数据
     */
    @Log(title = "字典数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<String> remove(@PathVariable Long[] ids)
    {
        dictDataService.deleteDictDataByIds(ids);
        return R.ok("删除成功");
    }
}
