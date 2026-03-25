package com.abclonal.product.dao.mapper;

import com.abclonal.product.dao.entity.TableMeta;

import java.util.List;

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
