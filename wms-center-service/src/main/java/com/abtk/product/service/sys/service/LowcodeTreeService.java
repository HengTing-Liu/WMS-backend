package com.abtk.product.service.sys.service;

import com.abtk.product.common.web.page.TableDataInfo;
import java.util.List;
import java.util.Map;

/**
 * 低代码树形查询服务接口
 */
public interface LowcodeTreeService {

    /**
     * 查询树形结构数据（分页）
     * @param tableCode 表标识
     * @param parentColumn 父节点列名
     * @param parentValue 父节点值（null表示根节点）
     * @param params 附加查询参数
     * @param pageNum 页码
     * @param pageSize 每页大小
     */
    TableDataInfo listTree(String tableCode, String parentColumn, Long parentValue,
                           Map<String, Object> params, Integer pageNum, Integer pageSize);

    /**
     * 查询所有树形节点（不分页，用于前端一次性加载构建树）
     * @param tableCode 表标识
     * @param parentColumn 父节点列名
     * @param params 附加查询参数
     */
    List<Map<String, Object>> listTreeAll(String tableCode, String parentColumn, Map<String, Object> params);

    /**
     * 查询指定节点的直接子节点数量
     * @param tableCode 表标识
     * @param parentColumn 父节点列名
     * @param parentValue 父节点值
     */
    Long countChildren(String tableCode, String parentColumn, Long parentValue);

    /**
     * 查询指定节点的直接子节点数量，支持按指定列过滤
     * <p>例如 filterColumn="location_grade", filterValues=["StorageSection", "存储分区"]
     * 只统计属于"存储分区"类型的子节点。</p>
     *
     * @param tableCode 表标识
     * @param parentColumn 父节点列名
     * @param parentValue 父节点值
     * @param filterColumn 过滤列名（允许 null/空表示不过滤）
     * @param filterValues 过滤值列表（允许 null/空表示不过滤）
     */
    Long countChildren(String tableCode, String parentColumn, Long parentValue,
                       String filterColumn, List<String> filterValues);

    /**
     * 获取节点的所有子孙节点ID（递归，不含自身）
     * MySQL 8.0+ WITH RECURSIVE CTE 实现
     * @param tableCode 表标识
     * @param parentColumn 父节点列名
     * @param nodeId 节点ID（作为根节点向下查找）
     */
    List<Long> getAllDescendantIds(String tableCode, String parentColumn, Long nodeId);

    /**
     * 获取节点的所有子孙节点（递归，不含自身）
     * @param tableCode 表标识
     * @param parentColumn 父节点列名
     * @param nodeId 节点ID
     */
    List<Map<String, Object>> getAllDescendants(String tableCode, String parentColumn, Long nodeId);

    /**
     * 批量启用/停用
     * @param tableCode 表标识
     * @param ids ID列表
     * @param enabled 目标状态（1=启用，0=停用）
     */
    void batchToggleStatus(String tableCode, List<Long> ids, Integer enabled);

    /**
     * 切换单个记录的状态
     * @param tableCode 表标识
     * @param id 记录ID
     * @param enabled 目标状态
     */
    void toggleStatus(String tableCode, Long id, Integer enabled);

    /**
     * 删除前检查业务占用
     * @param tableCode 表标识
     * @param id 待删除记录ID
     * @return 占用检查结果
     */
    Map<String, Object> checkDeleteOccupancy(String tableCode, Long id);
}