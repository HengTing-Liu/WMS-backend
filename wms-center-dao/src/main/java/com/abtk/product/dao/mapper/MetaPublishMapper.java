package com.abtk.product.dao.mapper;

import com.abtk.product.dao.entity.MetaPublish;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 发布主记录 Mapper
 */
public interface MetaPublishMapper {

    /**
     * 分页查询
     */
    List<MetaPublish> selectPage(@Param("tableCode") String tableCode,
                                 @Param("status") String status,
                                 @Param("publishBy") String publishBy,
                                 @Param("beginTime") LocalDateTime beginTime,
                                 @Param("endTime") LocalDateTime endTime);

    /**
     * 查询最大版本号
     */
    Long selectMaxVersionByTableCode(@Param("tableCode") String tableCode);

    /**
     * 根据发布编码查询
     */
    MetaPublish selectByPublishCode(@Param("publishCode") String publishCode);

    /**
     * 根据ID查询
     */
    MetaPublish selectById(@Param("id") Long id);

    /**
     * 新增
     */
    int insert(MetaPublish record);

    /**
     * 更新
     */
    int updateById(MetaPublish record);
}
