package com.abclonal.product.web.controller.system;

import com.abclonal.product.api.domain.request.sys.SysSerialNumberQueryRequest;
import com.abclonal.product.api.domain.request.sys.SysSerialNumberRequest;
import com.abclonal.product.api.domain.response.sys.SysSerialNumberResponse;
import com.abclonal.product.biz.system.SysSerialNumberBiz;
import com.abclonal.product.common.domain.R;
import com.abclonal.product.common.page.PageRequest;
import com.abclonal.product.common.page.PageResult;
import com.abclonal.product.common.utils.poi.ExcelUtil;
import com.abclonal.product.common.web.controller.BaseController;
import com.abclonal.product.common.web.page.TableDataInfo;
import com.abclonal.product.service.system.service.ISysSerialNumberService;
import com.abclonal.product.service.security.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 流水号规则表(SysSerialNumber)表控制层 - 匹配前端接口
 *
 * @author lht
 * @since 2026-03-09 15:20:00
 */
@RestController
@RequestMapping("/api/serial")
@Tag(name = "流水号管理")
public class SysSerialNumberController extends BaseController {

    @Autowired
    private ISysSerialNumberService sysSerialNumberService;

    @Autowired
    private SysSerialNumberBiz sysSerialNumberBiz;

    // ==================== 规则管理接口 ====================

    /**
     * 分页查询规则列表
     */
    @Operation(summary = "分页查询流水号规则列表")
    @GetMapping("/rule/list")
    public R<TableDataInfo> list(
            @Parameter(description = "分页参数") PageRequest pageRequest,
            @Parameter(description = "查询条件") SysSerialNumberQueryRequest queryRequest) {
        // 设置分页参数
        startPage();
        List<SysSerialNumberResponse> list = sysSerialNumberService.queryByCondition(queryRequest);
        return R.ok(getDataTable(list));
    }

    /**
     * 通过ID查询单条数据
     */
    @Operation(summary = "根据ID获取流水号规则详情")
    @GetMapping("/rule/{id}")
    public R<SysSerialNumberResponse> getInfo(
            @Parameter(description = "主键ID", required = true) @PathVariable("id") Long id) {
        return R.ok(sysSerialNumberService.queryById(id));
    }

    /**
     * 新增数据
     */
    @Operation(summary = "新增流水号规则")
    @PostMapping("/rule/add")
    public R<SysSerialNumberResponse> add(
            @Parameter(description = "流水号规则信息", required = true) @RequestBody SysSerialNumberRequest request) {
        String username = SecurityUtils.getUsername();
        return R.ok(sysSerialNumberService.insert(request, username));
    }

    /**
     * 修改数据
     */
    @Operation(summary = "修改流水号规则")
    @PutMapping("/rule/edit")
    public R<Integer> edit(
            @Parameter(description = "流水号规则信息", required = true) @RequestBody SysSerialNumberRequest request) {
        String username = SecurityUtils.getUsername();
        return R.ok(sysSerialNumberService.update(request, username));
    }

    /**
     * 删除数据
     */
    @Operation(summary = "删除流水号规则")
    @DeleteMapping("/rule/{ids}")
    public R<Integer> remove(
            @Parameter(description = "主键ID串", required = true) @PathVariable String ids) {
        String username = SecurityUtils.getUsername();
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return R.ok(sysSerialNumberService.logicDeleteBatchByIds(idList, username));
    }

    /**
     * 修改状态
     */
    @Operation(summary = "修改流水号规则状态")
    @PutMapping("/rule/changeStatus")
    public R<Integer> changeStatus(
            @Parameter(description = "状态信息", required = true) @RequestBody Map<String, Object> params) {
        String username = SecurityUtils.getUsername();
        Long id = Long.valueOf(params.get("id").toString());
        String status = params.get("status").toString();
        return R.ok(sysSerialNumberService.changeStatus(id, status, username));
    }

    /**
     * 导出数据
     */
    @Operation(summary = "导出流水号规则")
    @PostMapping("/rule/export")
    public void export(
            @Parameter(description = "查询条件") @RequestBody SysSerialNumberQueryRequest queryRequest,
            HttpServletResponse response) {
        sysSerialNumberService.export(queryRequest, response);
    }

    // ==================== 流水号生成接口 ====================

    /**
     * 生成流水号
     */
    @Operation(summary = "生成流水号")
    @PostMapping("/generate")
    public R<Map<String, Object>> generate(
            @Parameter(description = "生成参数", required = true) @RequestBody Map<String, Object> params) {
        String ruleName = (String) params.get("ruleName");
        String businessNo = (String) params.get("businessNo");
        String businessType = (String) params.get("businessType");
        String username = SecurityUtils.getUsername();
        
        String serialNo = sysSerialNumberBiz.generateSerialNumber(ruleName, username);
        
        Map<String, Object> result = new HashMap<>();
        result.put("serialNo", serialNo);
        result.put("ruleName", ruleName);
        result.put("businessNo", businessNo);
        result.put("businessType", businessType);
        
        return R.ok(result);
    }

    /**
     * 批量生成流水号
     */
    @Operation(summary = "批量生成流水号")
    @PostMapping("/batchGenerate")
    public R<List<Map<String, Object>>> batchGenerate(
            @Parameter(description = "生成参数", required = true) @RequestBody Map<String, Object> params) {
        String ruleName = (String) params.get("ruleName");
        Integer count = (Integer) params.get("count");
        String username = SecurityUtils.getUsername();
        
        List<String> serialNos = sysSerialNumberBiz.batchGenerateSerialNumber(ruleName, count, username);
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (String serialNo : serialNos) {
            Map<String, Object> item = new HashMap<>();
            item.put("serialNo", serialNo);
            item.put("ruleName", ruleName);
            result.add(item);
        }
        
        return R.ok(result);
    }

    // ==================== 生成记录接口（预留）====================

    /**
     * 查询生成记录列表（预留接口，后续可扩展）
     */
    @Operation(summary = "查询生成记录列表")
    @GetMapping("/record/list")
    public R<PageResult<Map<String, Object>>> recordList(
            @Parameter(description = "分页参数") PageRequest pageRequest,
            @Parameter(description = "查询条件") @RequestParam Map<String, Object> params) {
        // TODO: 如需记录功能，需创建记录表
        // 目前返回空列表
        return R.ok(new PageResult<>(java.util.Collections.emptyList(), 0));
    }

    /**
     * 导入流水号规则
     */
    @Operation(summary = "导入流水号规则")
    @PostMapping("/rule/importData")
    public R<String> importData(
            @Parameter(description = "导入文件") MultipartFile file,
            @Parameter(description = "是否支持更新") boolean updateSupport) throws Exception {
        ExcelUtil<SysSerialNumberRequest> util = new ExcelUtil<>(SysSerialNumberRequest.class);
        List<SysSerialNumberRequest> list = util.importExcel(file.getInputStream());
        String operName = SecurityUtils.getUsername();
        String message = sysSerialNumberBiz.importSerialNumber(list, updateSupport, operName);
        return R.ok(message);
    }

    /**
     * 下载导入模板
     */
    @Operation(summary = "下载导入模板")
    @PostMapping("/rule/downLoadTemplate")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil<SysSerialNumberRequest> util = new ExcelUtil<>(SysSerialNumberRequest.class);
        util.importTemplateExcel(response, "流水号规则导入模板");
    }
}
