package com.abtk.product.dao.mapper;

import com.abtk.product.dao.entity.TableOperation;

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
     * 根据ID查询
     */
    TableOperation selectById(Long id);

    /**
     * 新增
     */
    int insert(TableOperation operation);

    /**
     * 更新
     */
    int update(TableOperation operation);

    /**
     * 根据ID删除
     */
    int deleteById(Long id);

    /**
     * 批量更新排序号
     */
    int updateSortOrder(Long id, Integer sortOrder);
}
