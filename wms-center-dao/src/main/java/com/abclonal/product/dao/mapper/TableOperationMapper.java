package com.abclonal.product.dao.mapper;

import com.abclonal.product.dao.entity.TableOperation;

import java.util.List;

/**
 * 操作按钮 Mapper
 */
public interface TableOperationMapper {

    /**
     * 根据表编码查询操作按钮列表
     */
    List<TableOperation> selectByTableCode(String tableCode);

    /**
     * 新增
     */
    int insert(TableOperation operation);

    /**
     * 根据ID删除
     */
    int deleteById(Long id);
}
