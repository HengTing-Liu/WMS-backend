package com.abclonal.product.dao.entity;

import com.abclonal.product.common.annotation.Excel;
import com.abclonal.product.common.annotation.Excel.ColumnType;
import com.abclonal.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 同步日志表(SyncLog)实体类
 *
 * @author wms
 * @since 2026-04-03
 */
@Data
public class SyncLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Excel(name = "主键ID", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 接口编号
     */
    @Excel(name = "接口编号", type = Type.ALL)
    @Schema(description = "接口编号")
    private String interfaceCode;

    /**
     * 接口名称
     */
    @Excel(name = "接口名称", type = Type.ALL)
    @Schema(description = "接口名称")
    private String interfaceName;

    /**
     * 接口方式（HTTP/MQ/API）
     */
    @Excel(name = "接口方式", type = Type.ALL)
    @Schema(description = "接口方式")
    private String interfaceType;

    /**
     * 接口发起系统
     */
    @Excel(name = "发起系统", type = Type.ALL)
    @Schema(description = "接口发起系统")
    private String sourceSystem;

    /**
     * 接口接收系统
     */
    @Excel(name = "接收系统", type = Type.ALL)
    @Schema(description = "接口接收系统")
    private String targetSystem;

    /**
     * 开始时间
     */
    @Excel(name = "开始时间", type = Type.EXPORT, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @Excel(name = "结束时间", type = Type.EXPORT, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "结束时间")
    private Date endTime;

    /**
     * 数据类型
     */
    @Excel(name = "数据类型", type = Type.ALL)
    @Schema(description = "数据类型")
    private String dataType;

    /**
     * 标题
     */
    @Excel(name = "标题", type = Type.ALL)
    @Schema(description = "标题")
    private String title;

    /**
     * 单据编号
     */
    @Excel(name = "单据编号", type = Type.ALL)
    @Schema(description = "单据编号")
    private String bizNo;

    /**
     * 操作类型（新增/更新/删除）
     */
    @Excel(name = "操作类型", type = Type.ALL)
    @Schema(description = "操作类型")
    private String operationType;

    /**
     * 请求数据
     */
    @Schema(description = "请求数据")
    private String requestData;

    /**
     * 响应数据
     */
    @Schema(description = "响应数据")
    private String responseData;

    /**
     * 申方状态
     */
    @Excel(name = "申方状态", type = Type.ALL)
    @Schema(description = "申方状态")
    private String applyStatus;

    /**
     * 消方状态
     */
    @Excel(name = "消方状态", type = Type.ALL)
    @Schema(description = "消方状态")
    private String consumeStatus;

    /**
     * 错误信息
     */
    @Schema(description = "错误信息")
    private String errorMessage;

    /**
     * 重试次数
     */
    @Excel(name = "重试次数", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "重试次数")
    private Integer retryCount;

    /**
     * 是否删除（0-正常，1-删除）
     */
    @Excel(name = "是否删除", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "是否删除")
    private Integer isDeleted;
}
