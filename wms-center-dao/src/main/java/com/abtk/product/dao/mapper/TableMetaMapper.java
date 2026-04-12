package com.abtk.product.dao.mapper;

import com.abtk.product.dao.entity.TableMeta;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 表元数据 Mapper
 */
public interface TableMetaMapper {

    /**
     * 根据ID查询
     */
    TableMeta selectById(Long id);

    /**
     * 根据表编码查询
     */
    TableMeta selectByTableCode(String tableCode);

    /**
     * 查询所有
     */
    List<TableMeta> selectAll();

    /**
     * 根据模块查询
     */
    List<TableMeta> selectByModule(String module);

    /**
     * 条件查询列表
     */
    List<TableMeta> selectList(TableMeta condition);

    /**
     * 分页查询
     */
    List<TableMeta> selectPage(Map<String, Object> params);

    /**
     * 统计表编码重复（排除指定ID）
     */
    int selectCountByTableCodeExcludeId(@Param("tableCode") String tableCode, @Param("excludeId") Long excludeId);

    /**
     * 统计关联字段数量
     */
    int countColumnsByTableCode(@Param("tableCode") String tableCode, @Param("excludeDeleted") boolean excludeDeleted);

    /**
     * 统计关联操作数量
     */
    int countOperationsByTableCode(@Param("tableCode") String tableCode, @Param("excludeDeleted") boolean excludeDeleted);

    /**
     * 新增
     */
    int insert(TableMeta tableMeta);

    /**
     * 更新
     */
    int update(TableMeta tableMeta);

    /**
     * 删除
     */
    int deleteById(Long id);
}
