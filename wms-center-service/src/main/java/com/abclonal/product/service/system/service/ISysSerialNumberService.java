package com.abclonal.product.service.system.service;

import com.abclonal.product.api.domain.request.sys.SysSerialNumberQueryRequest;
import com.abclonal.product.api.domain.request.sys.SysSerialNumberRequest;
import com.abclonal.product.api.domain.response.sys.SysSerialNumberResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 流水号规则表(SysSerialNumber)表服务接口
 *
 * @author lht
 * @since 2026-03-09 15:20:00
 */
public interface ISysSerialNumberService {

    /**
     * 通过ID查询单条数据
     */
    SysSerialNumberResponse queryById(Long id);

    /**
     * 根据条件查询列表
     */
    List<SysSerialNumberResponse> queryByCondition(SysSerialNumberRequest request);

    /**
     * 根据查询条件查询列表（用于分页查询）
     */
    List<SysSerialNumberResponse> queryByCondition(SysSerialNumberQueryRequest queryRequest);

    /**
     * 根据条件查询总数
     */
    long count(SysSerialNumberRequest request);

    /**
     * 判断数据是否存在
     */
    boolean existsById(Long id);

    /**
     * 新增单条数据
     */
    SysSerialNumberResponse insert(SysSerialNumberRequest request, String createBy);

    /**
     * 修改数据
     */
    int update(SysSerialNumberRequest request, String updateBy);

    /**
     * 通过主键逻辑删除数据
     */
    boolean logicDeleteById(Long id, String updateBy);

    /**
     * 批量逻辑删除数据
     */
    int logicDeleteBatchByIds(List<Long> ids, String updateBy);

    /**
     * 修改状态
     */
    int changeStatus(Long id, String status, String updateBy);

    /**
     * 导出流水号规则
     */
    void export(SysSerialNumberQueryRequest queryRequest, HttpServletResponse response);
}
