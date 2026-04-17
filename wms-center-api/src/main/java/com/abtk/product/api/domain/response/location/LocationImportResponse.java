package com.abtk.product.api.domain.response.location;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 库位导入响应
 *
 * @author wms
 * @since 2026-04-15
 */
@Data
@Schema(description = "库位导入响应")
public class LocationImportResponse {

    /**
     * 成功数量
     */
    @Schema(description = "成功数量")
    private int successCount;

    /**
     * 失败数量
     */
    @Schema(description = "失败数量")
    private int failCount;

    /**
     * 错误列表
     */
    @Schema(description = "错误列表")
    private List<ImportError> errors = new ArrayList<>();

    /**
     * 导入错误详情
     */
    @Data
    @Schema(description = "导入错误详情")
    public static class ImportError {

        /**
         * 行号
         */
        @Schema(description = "行号")
        private int row;

        /**
         * 错误信息
         */
        @Schema(description = "错误信息")
        private String message;

        public ImportError() {
        }

        public ImportError(int row, String message) {
            this.row = row;
            this.message = message;
        }
    }

    /**
     * 创建成功响应
     */
    public static LocationImportResponse success(int successCount) {
        LocationImportResponse response = new LocationImportResponse();
        response.setSuccessCount(successCount);
        response.setFailCount(0);
        return response;
    }

    /**
     * 创建部分成功响应
     */
    public static LocationImportResponse partialSuccess(int successCount, int failCount, List<ImportError> errors) {
        LocationImportResponse response = new LocationImportResponse();
        response.setSuccessCount(successCount);
        response.setFailCount(failCount);
        response.setErrors(errors);
        return response;
    }

    /**
     * 创建完全失败响应
     */
    public static LocationImportResponse fail(List<ImportError> errors) {
        LocationImportResponse response = new LocationImportResponse();
        response.setSuccessCount(0);
        response.setFailCount(errors.size());
        response.setErrors(errors);
        return response;
    }
}
