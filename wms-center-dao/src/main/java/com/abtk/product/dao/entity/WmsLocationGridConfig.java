package com.abtk.product.dao.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 库位网格配置表(WmsLocationGridConfig)实体类
 *
 * @author wms
 * @since 2026-03-15
 */
@Data
public class WmsLocationGridConfig {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 库位ID
     */
    private Long locationId;

    /**
     * 网格行数
     */
    private Integer gridRows;

    /**
     * 网格列数
     */
    private Integer gridCols;

    /**
     * 网格配置JSON
     */
    private String gridConfigJson;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
