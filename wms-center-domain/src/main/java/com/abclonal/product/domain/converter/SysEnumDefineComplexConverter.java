package com.abclonal.product.domain.converter;

import com.abclonal.product.api.domain.request.sys.SysEnumDefineWithItemsRequest;
import com.abclonal.product.api.domain.request.sys.SysEnumDefineRequest;
import com.abclonal.product.api.domain.request.sys.SysEnumItemRequest;
import com.abclonal.product.api.domain.response.sys.SysEnumDefineWithItemsResponse;
import com.abclonal.product.dao.entity.SysEnumDefine;
import com.abclonal.product.dao.entity.SysEnumItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 枚举定义及明细复合操作转换器
 *
 * @author lht
 * @since 2026-03-06
 */
@Mapper
public interface SysEnumDefineComplexConverter {

    SysEnumDefineComplexConverter INSTANCE = Mappers.getMapper(SysEnumDefineComplexConverter.class);

    /**
     * Request转换为枚举定义实体
     *
     * @param request 请求对象
     * @return 枚举定义实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    SysEnumDefine toEntity(SysEnumDefineRequest request);

    /**
     * Request转换为枚定义实体（从复合请求中提取）
     *
     * @param request 复合请求对象
     * @return 枚举定义实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    SysEnumDefine toEntity(SysEnumDefineWithItemsRequest request);

    /**
     * EnumItemRequest列表转换为枚举明细实体列表
     *
     * @param requests 请求列表
     * @return 实体列表
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enumCode", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    List<SysEnumItem> toItemEntityList(List<SysEnumItemRequest> requests);

    /**
     * EnumItemRequest转换为枚举明细实体
     *
     * @param request 请求对象
     * @return 实体对象
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enumCode", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    SysEnumItem toItemEntity(SysEnumItemRequest request);

    /**
     * 枚举定义实体转换为响应对象
     *
     * @param enumDefine 枚举定义实体
     * @param enumItems 枚举明细列表
     * @return 响应对象
     */
    @Mapping(source = "enumDefine.id", target = "enumDefineId")
    @Mapping(source = "enumDefine.enumCode", target = "enumCode")
    @Mapping(source = "enumDefine.enumName", target = "enumName")
    SysEnumDefineWithItemsResponse toResponse(SysEnumDefine enumDefine, List<SysEnumItem> enumItems);

}
