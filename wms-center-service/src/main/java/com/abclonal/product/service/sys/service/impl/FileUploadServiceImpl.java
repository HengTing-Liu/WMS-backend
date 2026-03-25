package com.abclonal.product.service.sys.service.impl;

import com.abclonal.product.api.domain.response.sys.FileUploadResponse;
import com.abclonal.product.service.sys.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传服务实现
 * TODO: 待考虑接入OSS存储
 */
@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${file.upload.path:/tmp/wms-upload}")
    private String uploadPath;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    @Override
    public FileUploadResponse upload(MultipartFile file) {
        log.info("开始上传文件: {}", file.getOriginalFilename());

        if (file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("文件大小超过限制（最大10MB）");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            String newFilename = UUID.randomUUID().toString() + fileExtension;

            String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            String relativePath = datePath + "/" + newFilename;
            String fullPath = uploadPath + "/" + relativePath;

            Path targetPath = Paths.get(fullPath);
            Files.createDirectories(targetPath.getParent());
            file.transferTo(targetPath.toFile());

            FileUploadResponse response = new FileUploadResponse();
            response.setFileId(System.currentTimeMillis());
            response.setFileName(originalFilename);
            response.setFilePath(relativePath);
            response.setFileSize(file.getSize());
            response.setFileType(fileExtension);
            response.setUrl("/api/system/file/" + relativePath);
            response.setUploadTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            log.info("文件上传成功: {}", relativePath);
            return response;
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<FileUploadResponse> uploadBatch(List<MultipartFile> files) {
        log.info("开始批量上传文件: {} 个", files.size());
        List<FileUploadResponse> responses = new ArrayList<>();
        for (MultipartFile file : files) {
            responses.add(upload(file));
        }
        return responses;
    }

    @Override
    public void delete(Long fileId) {
        log.info("删除文件, fileId={}", fileId);
        // TODO: 从数据库查询文件路径，然后删除文件
        // 当前为占位实现
    }

    private String getFileExtension(String filename) {
        if (StringUtils.isBlank(filename)) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex);
        }
        return "";
    }
}
