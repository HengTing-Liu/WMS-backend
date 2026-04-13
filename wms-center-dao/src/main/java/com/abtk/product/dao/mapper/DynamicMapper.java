package com.abtk.product.dao.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 动态SQL Mapper接口
 * 支持基于表名动态执行CRUD操作
 *
 * 【安全警示】
 * - tableCode、pkColumn、deleteColumn、field 等标识符参数必须通过 SqlInjectionValidator 校验
 * - 禁止将未经校验的用户输入直接传入本接口
 * - insert(String tableCode, String sql) 和 update(String tableCode, String sql) 已废弃，请使用 insertParam/updateParam
 */
public interface DynamicMapper {

    /**
     * 动态查询列表（动态删除列 + 动态搜索参数）
     * @param tableCode 表名（必须通过 SqlInjectionValidator.validateTable 校验）
     * @param params 查询参数（字段名keys必须通过 SqlInjectionValidator.validateFieldFormat 校验）
     * @param deleteColumn 删除标志列名（必须通过 SqlInjectionValidator.validateFieldFormat 校验）
     * @param dataScope 数据权限 raw SQL 片段（由 CrudPermissionUtil 注入，直接拼入 WHERE，非查询参数）
     */
    List<Map<String, Object>> selectList(@Param("tableCode") String tableCode, @Param("params") Map<String, Object> params, @Param("deleteColumn") String deleteColumn, @Param("dataScope") String dataScope);

    /**
     * 查询所有（支持自定义删除列名）
     * @param tableCode 表名（必须通过 SqlInjectionValidator.validateTable 校验）
     * @param params 查询参数（字段名keys必须通过 SqlInjectionValidator.validateFieldFormat 校验）
     * @param deleteColumn 删除标志列名（必须通过 SqlInjectionValidator.validateFieldFormat 校验）
     * @param dataScope 数据权限 raw SQL 片段（由 CrudPermissionUtil 注入）
     */
    List<Map<String, Object>> selectAll(@Param("tableCode") String tableCode, @Param("params") Map<String, Object> params, @Param("deleteColumn") String deleteColumn, @Param("dataScope") String dataScope);

    /**
     * 根据ID查询
     * @param tableCode 表名（必须通过 SqlInjectionValidator.validateTable 校验）
     */
    Map<String, Object> selectById(@Param("tableCode") String tableCode, @Param("id") Long id);

    /**
     * 根据自定义主键列查询
     * @param tableCode 表名（必须通过 SqlInjectionValidator.validateTable 校验）
     * @param pkColumn 主键列名（必须通过 SqlInjectionValidator.validateFieldFormat 校验）
     * @param pkValue 主键值
     */
    Map<String, Object> selectByIdWithColumn(@Param("tableCode") String tableCode, @Param("pkColumn") String pkColumn, @Param("pkValue") Long pkValue);

    /**
     * 插入数据（SQL字符串方式）【已废弃】
     * 风险：直接拼接SQL字符串，存在高注入风险
     * 替代：使用 insertParam 方法
     * @param tableCode 表名
     * @param sql 完整SQL字符串
     * @deprecated 请使用 insertParam(String tableCode, List<String> columns, List<Object> values)
     */
    @Deprecated
    int insert(@Param("tableCode") String tableCode, @Param("sql") String sql);

    /**
     * 插入数据（参数化方式）
     * @param tableCode 表名（必须通过 SqlInjectionValidator.validateTable 校验）
     * @param columns 列名列表（必须通过 SqlInjectionValidator.validateFieldFormat 校验每个元素）
     * @param values 值列表
     */
    int insertParam(@Param("tableCode") String tableCode, @Param("columns") List<String> columns, @Param("values") List<Object> values);

    /**
     * 更新数据（SQL字符串方式）【已废弃】
     * 风险：直接拼接SQL字符串，存在高注入风险
     * 替代：使用 updateParam 方法
     * @param tableCode 表名
     * @param sql 完整SQL字符串
     * @deprecated 请使用 updateParam(String tableCode, Map<String, Object> data, String pkColumn, Long pkValue)
     */
    @Deprecated
    int update(@Param("tableCode") String tableCode, @Param("sql") String sql);

    /**
     * 更新数据（参数化方式）
     * @param tableCode 表名（必须通过 SqlInjectionValidator.validateTable 校验）
     * @param data 数据Map（字段名keys必须通过 SqlInjectionValidator.validateFieldFormat 校验）
     * @param pkColumn 主键列名（必须通过 SqlInjectionValidator.validateFieldFormat 校验）
     * @param pkValue 主键值
     */
    int updateParam(@Param("tableCode") String tableCode, @Param("data") Map<String, Object> data, @Param("pkColumn") String pkColumn, @Param("pkValue") Long pkValue);

    /**
     * 逻辑删除（支持自定义删除列名）
     * @param tableCode 表名（必须通过 SqlInjectionValidator.validateTable 校验）
     * @param pkColumn 主键列名（必须通过 SqlInjectionValidator.validateFieldFormat 校验）
     * @param pkValue 主键值
     * @param deleteColumn 删除标志列名（必须通过 SqlInjectionValidator.validateFieldFormat 校验）
     */
    int logicDelete(@Param("tableCode") String tableCode, @Param("pkColumn") String pkColumn, @Param("pkValue") Long pkValue, @Param("deleteColumn") String deleteColumn);

    /**
     * 批量逻辑删除（支持自定义删除列名）
     * @param tableCode 表名（必须通过 SqlInjectionValidator.validateTable 校验）
     * @param pkColumn 主键列名（必须通过 SqlInjectionValidator.validateFieldFormat 校验）
     * @param ids 主键值列表
     * @param deleteColumn 删除标志列名（必须通过 SqlInjectionValidator.validateFieldFormat 校验）
     */
    int batchLogicDelete(@Param("tableCode") String tableCode, @Param("pkColumn") String pkColumn, @Param("ids") List<Long> ids, @Param("deleteColumn") String deleteColumn);

    /**
     * 检查唯一性
     * @param tableCode 表名（必须通过 SqlInjectionValidator.validateTable 校验）
     * @param field 字段名（必须通过 SqlInjectionValidator.validateField 校验）
     * @param value 字段值
     * @param excludeId 排除的ID（用于编辑时排除自身）
     */
    Long checkUnique(@Param("tableCode") String tableCode, @Param("field") String field, @Param("value") String value, @Param("excludeId") Long excludeId);

    /**
     * 查询最后插入的ID
     */
    Map<String, Object> selectLastInsertId();

    /**
     * 查询表的所有列信息
     * @param tableCode 表名（必须通过 SqlInjectionValidator.validateTable 校验）
     */
    List<Map<String, Object>> selectTableColumns(@Param("tableCode") String tableCode);

    /**
     * 执行自定义查询SQL【已废弃+未实现】
     * 风险：直接拼接SQL字符串
     * 注意：此方法未在XML中实现映射，调用会出错
     * @deprecated 此方法未实现，请勿使用
     */
    @Deprecated
    List<Map<String, Object>> executeQuery(@Param("sql") String sql, @Param("params") Map<String, Object> params);
}
