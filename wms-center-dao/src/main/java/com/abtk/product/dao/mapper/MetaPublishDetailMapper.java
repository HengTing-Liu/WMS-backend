package com.abtk.product.dao.mapper;

import com.abtk.product.dao.entity.MetaPublishDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 发布 SQL 明细 Mapper
 */
public interface MetaPublishDetailMapper {

    /**
     * 根据发布ID查询明细
     */
    List<MetaPublishDetail> selectByPublishId(@Param("publishId") Long publishId);

    /**
     * 批量新增
     */
    int batchInsert(@Param("details") List<MetaPublishDetail> details);

    /**
     * 更新单条执行结果
     */
    int updateById(MetaPublishDetail record);

    /**
     * 根据ID查询
     */
    MetaPublishDetail selectById(@Param("id") Long id);
}
