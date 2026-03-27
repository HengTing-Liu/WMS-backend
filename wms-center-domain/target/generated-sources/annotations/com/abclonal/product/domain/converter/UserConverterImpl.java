package com.abclonal.product.domain.converter;

import com.abclonal.product.api.domain.request.SysUserRequest;
import com.abclonal.product.api.domain.response.SysUserResponse;
import com.abclonal.product.dao.entity.SysDept;
import com.abclonal.product.dao.entity.SysUser;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-27T10:16:06+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 1.8.0_482 (Private Build)"
)
public class UserConverterImpl implements UserConverter {

    @Override
    public SysUser requestToSysUser(SysUserRequest vo) {
        if ( vo == null ) {
            return null;
        }

        SysUser sysUser = new SysUser();

        sysUser.setCreateBy( vo.getCreateBy() );
        sysUser.setCreateTime( vo.getCreateTime() );
        sysUser.setUpdateBy( vo.getUpdateBy() );
        sysUser.setUpdateTime( vo.getUpdateTime() );
        Map<String, Object> map = vo.getParams();
        if ( map != null ) {
            sysUser.setParams( new LinkedHashMap<String, Object>( map ) );
        }
        sysUser.setUserId( vo.getUserId() );
        sysUser.setDeptId( vo.getDeptId() );
        sysUser.setNickName( vo.getNickName() );
        sysUser.setUserName( vo.getUserName() );
        sysUser.setEmail( vo.getEmail() );
        sysUser.setPhonenumber( vo.getPhonenumber() );
        sysUser.setSex( vo.getSex() );
        sysUser.setPassword( vo.getPassword() );
        sysUser.setStatus( vo.getStatus() );
        Long[] roleIds = vo.getRoleIds();
        if ( roleIds != null ) {
            sysUser.setRoleIds( Arrays.copyOf( roleIds, roleIds.length ) );
        }
        Long[] postIds = vo.getPostIds();
        if ( postIds != null ) {
            sysUser.setPostIds( Arrays.copyOf( postIds, postIds.length ) );
        }
        sysUser.setRoleId( vo.getRoleId() );

        return sysUser;
    }

    @Override
    public SysUserResponse sysUserToResponse(SysUser sysUser) {
        if ( sysUser == null ) {
            return null;
        }

        SysUserResponse sysUserResponse = new SysUserResponse();

        sysUserResponse.setDeptName( sysUserDeptDeptName( sysUser ) );
        sysUserResponse.setCreateBy( sysUser.getCreateBy() );
        sysUserResponse.setCreateTime( sysUser.getCreateTime() );
        sysUserResponse.setUpdateBy( sysUser.getUpdateBy() );
        sysUserResponse.setUpdateTime( sysUser.getUpdateTime() );
        sysUserResponse.setRemark( sysUser.getRemark() );
        sysUserResponse.setUserId( sysUser.getUserId() );
        sysUserResponse.setDeptId( sysUser.getDeptId() );
        sysUserResponse.setUserName( sysUser.getUserName() );
        sysUserResponse.setNickName( sysUser.getNickName() );
        sysUserResponse.setEmail( sysUser.getEmail() );
        sysUserResponse.setPhonenumber( sysUser.getPhonenumber() );
        sysUserResponse.setSex( sysUser.getSex() );
        sysUserResponse.setStatus( sysUser.getStatus() );
        sysUserResponse.setRoleId( sysUser.getRoleId() );

        return sysUserResponse;
    }

    private String sysUserDeptDeptName(SysUser sysUser) {
        if ( sysUser == null ) {
            return null;
        }
        SysDept dept = sysUser.getDept();
        if ( dept == null ) {
            return null;
        }
        String deptName = dept.getDeptName();
        if ( deptName == null ) {
            return null;
        }
        return deptName;
    }
}
