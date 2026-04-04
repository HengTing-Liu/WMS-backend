package com.abtk.product.web.controller.generator;

import com.abtk.product.api.generator.CodeGenerateConfig;
import com.abtk.product.api.generator.CodeTemplate;
import com.abtk.product.api.generator.GeneratedFile;
import com.abtk.product.service.generator.CodeGeneratorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 代码生成控制器

 * @author backend1
 * @date 2025-06-18
 */
@RestController
@RequestMapping("/lowcode/code-generator")
public class CodeGeneratorController {

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    /**
     * 获取所有代码模板
     *
     * @return 模板列表
     */
    @GetMapping("/templates")
    public List<CodeTemplate> getTemplates() {
        return codeGeneratorService.getTemplates();
    }

    /**
     * 预览代码
     *
     * @param config 生成配置
     * @return 生成的文件列表
     */
    @PostMapping("/preview")
    public List<GeneratedFile> previewCode(@RequestBody CodeGenerateConfig config) {
        return codeGeneratorService.previewCode(config);
    }
}
