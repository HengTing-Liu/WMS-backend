package com.abtk.product.dao.mapper;

import com.abtk.product.dao.entity.FormGroupMeta;

import java.util.List;

public interface FormGroupMetaMapper {
    List<FormGroupMeta> selectByTableCode(String tableCode);

    FormGroupMeta selectById(Long id);

    int insert(FormGroupMeta formGroupMeta);

    int update(FormGroupMeta formGroupMeta);

    int updateSortOrder(Long id, Integer sortOrder);

    int deleteById(Long id);

    int deleteByTableCode(String tableCode);
}
