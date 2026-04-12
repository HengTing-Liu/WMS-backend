package com.abtk.product.dao.mapper;

import com.abtk.product.dao.entity.MetaPublishSnapshot;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 发布快照 Mapper
 */
public interface MetaPublishSnapshotMapper {

    /**
     * 根据发布ID查询快照
     */
    List<MetaPublishSnapshot> selectByPublishId(@Param("publishId") Long publishId);

    /**
     * 查询表最新 BEFORE 快照
     */
    MetaPublishSnapshot selectLatestBeforeByTableCode(@Param("tableCode") String tableCode);

    /**
     * 新增
     */
    int insert(MetaPublishSnapshot record);
}
