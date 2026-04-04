package com.abtk.product.dao.mapper;

import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 低代码动态查询 Mapper
 *
 * 【安全警示】
 * - tableCode、pkColumn、deleteColumn、field 等标识符参数必须通过 SqlInjectionValidator 校验
 * - 禁止将未经校验的用户输入直接传入本接口
 * - 所有占位符 ${} 用于 SQL 标识符（表名/列名），值用 #{} 参数化
 */
public interface LowcodeMapper {

    /**
     * 树形查询：查询指定父节点的所有子节点
     * @param tableCode 表名（必须通过 SqlInjectionValidator.validateTable 校验）
     * @param deleteColumn 删除标志列名（必须通过 SqlInjectionValidator.validateFieldFormat 校验）
     * @param parentColumn 父节点列名（必须通过 SqlInjectionValidator.validateFieldFormat 校验）
     * @param parentValue 父节点值（传 null 表示查询根节点）
     */
    List<Map<String, Object>> selectByParent(
            @Param("tableCode") String tableCode,
            @Param("deleteColumn") String deleteColumn,
            @Param("parentColumn") String parentColumn,
            @Param("parentValue") Long parentValue);

    /**
     * 树形查询：查询所有节点（用于前端一次性加载后本地构建树）
     * @param tableCode 表名（必须通过 SqlInjectionValidator.validateTable 校验）
     * @param deleteColumn 删除标志列名
     * @param parentColumn 父节点列名
     */
    List<Map<String, Object>> selectTreeAll(
            @Param("tableCode") String tableCode,
            @Param("deleteColumn") String deleteColumn,
            @Param("parentColumn") String parentColumn);

    /**
     * 根据多个ID批量查询
     * @param tableCode 表名（必须通过 SqlInjectionValidator.validateTable 校验）
     * @param pkColumn 主键列名
     * @param ids ID列表
     */
    List<Map<String, Object>> selectByIds(
            @Param("tableCode") String tableCode,
            @Param("pkColumn") String pkColumn,
            @Param("ids") List<Long> ids);

    /**
     * 统计子节点数量
     * @param tableCode 表名
     * @param deleteColumn 删除标志列名
     * @param parentColumn 父节点列名
     * @param parentValue 父节点值
     */
    Long countByParent(
            @Param("tableCode") String tableCode,
            @Param("deleteColumn") String deleteColumn,
            @Param("parentColumn") String parentColumn,
            @Param("parentValue") Long parentValue);

    /**
     * 查询所有子节点的ID（递归，MySQL 8.0+ CTE 实现）
     * @param tableCode 表名
     * @param deleteColumn 删除标志列名
     * @param parentColumn 父节点列名
     * @param nodeId 节点ID（作为根节点向下查找）
     */
    List<Long> selectAllChildIds(
            @Param("tableCode") String tableCode,
            @Param("deleteColumn") String deleteColumn,
            @Param("parentColumn") String parentColumn,
            @Param("nodeId") Long nodeId);
}