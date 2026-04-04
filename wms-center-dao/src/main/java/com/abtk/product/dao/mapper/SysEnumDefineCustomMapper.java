package com.abtk.product.dao.mapper;

import com.abtk.product.dao.entity.SysEnumDefine;
import com.abtk.product.dao.entity.SysEnumItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 枚举定义自定义Mapper接口
 * 用于处理枚举定义和明细的复合操作
 *
 * @author lht
 * @since 2026-03-06
 */
@Mapper
public interface SysEnumDefineCustomMapper {

    /**
     * 插入枚举定义
     *
     * @param sysEnumDefine 枚举定义实体
     * @return 影响行数
     */
    int insertEnumDefine(SysEnumDefine sysEnumDefine);

    /**
     * 批量插入枚举明细
     *
     * @param enumItems 枚举明细列表
     * @return 影响行数
     */
    int batchInsertEnumItems(@Param("list") List<SysEnumItem> enumItems);

}
