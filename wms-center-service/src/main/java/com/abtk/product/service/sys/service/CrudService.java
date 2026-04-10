package com.abtk.product.service.sys.service;

import com.abtk.product.common.web.page.TableDataInfo;

import java.util.List;
import java.util.Map;

/**
 * 通用 CRUD 服务接口
 */
public interface CrudService {

    /**
     * 导出数据列表
     * @param tableCode 表标识
     * @param params 查询参数
     * @return 导出数据（包含字段配置信息）
     */
    Map<String, Object> exportList(String tableCode, Map<String, Object> params);

    /**
     * 分页查询列表
     */
    TableDataInfo list(String tableCode, Map<String, Object> params, Integer pageNum, Integer pageSize);

    /**
     * 查询所有数据
     */
    List<Map<String, Object>> listAll(String tableCode, Map<String, Object> params);

    /**
     * 根据ID查询
     */
    Map<String, Object> getById(String tableCode, Long id);

    /**
     * 新增记录
     */
    Long create(String tableCode, Map<String, Object> data);

    /**
     * 更新记录
     */
    void update(String tableCode, Long id, Map<String, Object> data);

    /**
     * 删除记录
     */
    void delete(String tableCode, Long id);

    /**
     * 批量删除
     */
    void batchDelete(String tableCode, List<Long> ids);

    /**
     * 检查唯一性
     */
    boolean checkUnique(String tableCode, String field, String value, Long excludeId);
}
