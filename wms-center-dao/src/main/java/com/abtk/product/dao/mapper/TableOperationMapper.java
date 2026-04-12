package com.abtk.product.dao.mapper;

import com.abtk.product.dao.entity.TableOperation;

import java.util.List;

/**
 * Operation button mapper
 */
public interface TableOperationMapper {

    List<TableOperation> selectByTableCode(String tableCode);

    TableOperation selectById(Long id);

    int insert(TableOperation operation);

    int update(TableOperation operation);

    int deleteById(Long id);

    int deleteByTableCode(String tableCode);

    int updateSortOrder(Long id, Integer sortOrder);
}
