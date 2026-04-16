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

    int updateSortOrder(Long id, Integer sortOrder);

    /**
     * 批量更新字段的分组信息
     */
    int batchUpdateSectionByIds(@org.apache.ibatis.annotations.Param("ids") List<Long> ids,
                                @org.apache.ibatis.annotations.Param("sectionKey") String sectionKey,
                                @org.apache.ibatis.annotations.Param("sectionTitle") String sectionTitle,
                                @org.apache.ibatis.annotations.Param("sectionOrder") Integer sectionOrder,
                                @org.apache.ibatis.annotations.Param("sectionType") String sectionType,
                                @org.apache.ibatis.annotations.Param("sectionOpen") Integer sectionOpen);

    int batchUpdateColSpanByIds(@org.apache.ibatis.annotations.Param("ids") List<Long> ids,
                                @org.apache.ibatis.annotations.Param("colSpan") Integer colSpan);

    /**
     * 根据分组编码和表编码更新字段的分组 key
     */
    int updateSectionKeyByGroupCode(@org.apache.ibatis.annotations.Param("tableCode") String tableCode,
                                    @org.apache.ibatis.annotations.Param("oldGroupCode") String oldGroupCode,
                                    @org.apache.ibatis.annotations.Param("newGroupCode") String newGroupCode);

    /**
     * 根据ID删除
     */
    int deleteById(Long id);

    /**
     * 根据表编码删除
     */
    int deleteByTableCode(String tableCode);
}
