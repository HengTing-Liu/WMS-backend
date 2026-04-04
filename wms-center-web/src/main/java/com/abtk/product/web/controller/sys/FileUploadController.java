package com.abtk.product.web.controller.sys;

import com.abtk.product.api.domain.response.sys.FileUploadResponse;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.web.controller.BaseController;
import com.abtk.product.service.sys.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件上传Controller
 * 提供文件上传、删除等接口，支持Upload组件的文件上传功能
 */
@Slf4j
@Tag(name = "文件上传", description = "文件上传管理接口")
@RestController
@RequestMapping("/api/system/file")
public class FileUploadController extends BaseController {

    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 单文件上传
     */
    @Operation(summary = "单文件上传", description = "上传单个文件，支持文件类型和大小校验")
    @PostMapping("/upload")
    public R<FileUploadResponse> upload(
            @Parameter(description = "上传的文件", required = true)
            @RequestParam("file") MultipartFile file) {
        FileUploadResponse response = fileUploadService.upload(file);
        return R.ok(response);
    }

    /**
     * 批量文件上传
     */
    @Operation(summary = "批量文件上传", description = "上传多个文件")
    @PostMapping("/upload/batch")
    public R<List<FileUploadResponse>> uploadBatch(
            @Parameter(description = "上传的文件列表", required = true)
            @RequestParam("files") List<MultipartFile> files) {
        List<FileUploadResponse> responses = fileUploadService.uploadBatch(files);
        return R.ok(responses);
    }

    /**
     * 删除文件
     */
    @Operation(summary = "删除文件", description = "根据文件ID删除文件")
    @DeleteMapping("/{fileId}")
    public R<Void> delete(
            @Parameter(description = "文件ID", required = true)
            @PathVariable Long fileId) {
        fileUploadService.delete(fileId);
        return R.ok();
    }
}
