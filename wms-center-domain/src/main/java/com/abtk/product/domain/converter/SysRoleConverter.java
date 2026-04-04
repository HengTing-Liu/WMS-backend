package com.abtk.product.domain.converter;

import com.abtk.product.api.domain.request.sys.SysRoleRequest;
import com.abtk.product.api.domain.response.sys.SysRoleResponse;
import com.abtk.product.dao.entity.SysRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 角色转换器 - 使用 MapStruct 自动映射
 *
 * @author lht
 * @time: 2026/3/11 19:20
 */
@Mapper
public interface SysRoleConverter {
    SysRoleConverter INSTANCE = Mappers.getMapper(SysRoleConverter.class);

    /**
     * Request 转 Entity
     */
    @Mappings({
        @Mapping(source = "menuIds", target = "menuIds"),
        @Mapping(source = "remark", target = "remark")
    })
    SysRole requestToEntity(SysRoleRequest request);

    /**
     * Entity 转 Response
     */
    SysRoleResponse entityToResponse(SysRole entity);

    /**
     * Entity 列表转 Response 列表
     */
    List<SysRoleResponse> entityListToResponseList(List<SysRole> entityList);
}
