package com.abtk.product.api.domain.response.lowcode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "删除前业务占用检查结果")
public class OccupancyCheckVO {

    @Schema(description = "是否可删除（无任何占用时为 true）")
    private Boolean deletable;

    @Schema(description = "主记录名称")
    private String recordName;

    @Schema(description = "占用明细列表")
    private List<OccupancyItem> items;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "占用项")
    public static class OccupancyItem {
        @Schema(description = "引用表名")
        private String refTable;
        @Schema(description = "引用表中文名")
        private String refTableName;
        @Schema(description = "被引用次数")
        private Long count;
    }
}
