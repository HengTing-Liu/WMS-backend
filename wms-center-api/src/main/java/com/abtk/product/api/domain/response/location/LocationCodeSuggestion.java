package com.abtk.product.api.domain.response.location;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 库位编码建议响应
 * <p>
 * 根据父节点和库位类型，自动计算下一个可用编码。
 * 编码格式：{仓库前缀}-{层级路径}-{类型前缀}{序号}
 * 示例：
 * - 根节点（存储区）：WH01-S001
 * - 子节点（货架）：WH01-S001-F001
 * - 孙节点（层）：WH01-S001-F001-L001
 *
 * @author wms
 * @since 2026-04-05
 */
@Data
@Schema(description = "库位编码建议响应")
public class LocationCodeSuggestion {

    /**
     * 建议的库位编码
     */
    @Schema(description = "建议的库位编码，如 'A001'")
    private String suggestedCode;

    /**
     * 当前同级最大序号
     */
    @Schema(description = "当前同级最大序号（用于提示）")
    private Integer currentMaxSerial;

    /**
     * 下一序号
     */
    @Schema(description = "下一可用序号")
    private Integer nextSerial;

    /**
     * 父节点编码（用于展示当前层级路径）
     */
    @Schema(description = "父节点编码，根节点为空")
    private String parentCode;

    /**
     * 父节点ID
     */
    @Schema(description = "父节点ID")
    private Long parentId;

    /**
     * 当前层级深度（从1开始）
     */
    @Schema(description = "当前层级深度")
    private Integer currentLevel;

    /**
     * 编码前缀（由类型决定）
     */
    @Schema(description = "编码前缀")
    private String codePrefix;

    /**
     * 序号位数（固定4位）
     */
    @Schema(description = "序号位数，固定4位")
    private Integer serialLength;

    /**
     * 完整的编码路径（从根到父节点）
     */
    @Schema(description = "完整编码路径，从根到父节点")
    private String fullPath;

    public LocationCodeSuggestion() {
        this.serialLength = 4;
    }
}
