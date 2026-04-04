package com.abtk.product.dao.mapper;

import com.abtk.product.dao.entity.ColumnMeta;

import java.util.List;

/**
 * 字段元数据 Mapper
 */
public interface ColumnMetaMapper {

    /**
     * 根据表编码查询字段列表
     */
    List<ColumnMeta> selectByTableCode(String tableCode);

    /**
     * 根据ID查询
     */
    ColumnMeta selectById(Long id);

    /**
     * 新增
     */
    int insert(ColumnMeta columnMeta);

    /**
     * 更新（用于更新 dict_type 等字段）
     */
    int update(ColumnMeta columnMeta);

    /**
     * 根据ID删除
     */
    int deleteById(Long id);

    /**
     * 根据表编码删除
     */
    int deleteByTableCode(String tableCode);
}
