package com.abtk.product.api.domain.response.sys;

import lombok.Data;

/**
 * 文件上传响应
 */
@Data
public class FileUploadResponse {
    /**
     * 文件ID
     */
    private Long fileId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 访问URL
     */
    private String url;

    /**
     * 上传时间
     */
    private String uploadTime;
}
