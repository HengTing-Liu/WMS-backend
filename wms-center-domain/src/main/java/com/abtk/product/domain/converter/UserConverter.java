package com.abtk.product.domain.converter;

import com.abtk.product.api.domain.request.SysUserRequest;
import com.abtk.product.api.domain.response.SysUserResponse;
import com.abtk.product.dao.entity.SysUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 *
 *
 * @description:
 * @author: 75618
 * @time: 2026/2/6 15:47
 *
 */
@Mapper
public interface  UserConverter {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    SysUser requestToSysUser(SysUserRequest vo);

    @Mapping(source = "dept.deptName", target = "deptName")
    @Mapping(source = "dept.deptCode", target = "deptCode")
    @Mapping(source = "nickName", target = "code")
    SysUserResponse sysUserToResponse(SysUser sysUser);

}
