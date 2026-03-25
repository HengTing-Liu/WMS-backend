package com.abclonal.product.service.sys.service;

import com.abclonal.product.api.domain.response.sys.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件上传服务接口
 */
public interface FileUploadService {

    /**
     * 单文件上传
     */
    FileUploadResponse upload(MultipartFile file);

    /**
     * 批量文件上传
     */
    List<FileUploadResponse> uploadBatch(List<MultipartFile> files);

    /**
     * 删除文件
     */
    void delete(Long fileId);
}
