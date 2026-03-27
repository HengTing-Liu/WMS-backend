package com.abclonal.product.domain.converter;

import com.abclonal.product.api.domain.request.sys.SysRoleRequest;
import com.abclonal.product.api.domain.response.sys.SysRoleResponse;
import com.abclonal.product.dao.entity.SysRole;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-27T10:16:06+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 1.8.0_482 (Private Build)"
)
public class SysRoleConverterImpl implements SysRoleConverter {

    @Override
    public SysRole requestToEntity(SysRoleRequest request) {
        if ( request == null ) {
            return null;
        }

        SysRole sysRole = new SysRole();

        Long[] menuIds = request.getMenuIds();
        if ( menuIds != null ) {
            sysRole.setMenuIds( Arrays.copyOf( menuIds, menuIds.length ) );
        }
        sysRole.setRemark( request.getRemark() );
        sysRole.setCreateBy( request.getCreateBy() );
        sysRole.setCreateTime( request.getCreateTime() );
        sysRole.setUpdateBy( request.getUpdateBy() );
        sysRole.setUpdateTime( request.getUpdateTime() );
        Map<String, Object> map = request.getParams();
        if ( map != null ) {
            sysRole.setParams( new LinkedHashMap<String, Object>( map ) );
        }
        sysRole.setRoleId( request.getRoleId() );
        sysRole.setRoleName( request.getRoleName() );
        sysRole.setRoleKey( request.getRoleKey() );
        sysRole.setRoleSort( request.getRoleSort() );
        sysRole.setDataScope( request.getDataScope() );
        if ( request.getMenuCheckStrictly() != null ) {
            sysRole.setMenuCheckStrictly( request.getMenuCheckStrictly() );
        }
        if ( request.getDeptCheckStrictly() != null ) {
            sysRole.setDeptCheckStrictly( request.getDeptCheckStrictly() );
        }
        sysRole.setStatus( request.getStatus() );
        Long[] deptIds = request.getDeptIds();
        if ( deptIds != null ) {
            sysRole.setDeptIds( Arrays.copyOf( deptIds, deptIds.length ) );
        }

        return sysRole;
    }

    @Override
    public SysRoleResponse entityToResponse(SysRole entity) {
        if ( entity == null ) {
            return null;
        }

        SysRoleResponse sysRoleResponse = new SysRoleResponse();

        sysRoleResponse.setRoleId( entity.getRoleId() );
        sysRoleResponse.setRoleName( entity.getRoleName() );
        sysRoleResponse.setRoleKey( entity.getRoleKey() );
        sysRoleResponse.setRoleSort( entity.getRoleSort() );
        sysRoleResponse.setDataScope( entity.getDataScope() );
        sysRoleResponse.setMenuCheckStrictly( entity.isMenuCheckStrictly() );
        sysRoleResponse.setDeptCheckStrictly( entity.isDeptCheckStrictly() );
        sysRoleResponse.setStatus( entity.getStatus() );
        sysRoleResponse.setCreateBy( entity.getCreateBy() );
        sysRoleResponse.setCreateTime( entity.getCreateTime() );
        sysRoleResponse.setUpdateBy( entity.getUpdateBy() );
        sysRoleResponse.setUpdateTime( entity.getUpdateTime() );
        sysRoleResponse.setRemark( entity.getRemark() );
        Long[] menuIds = entity.getMenuIds();
        if ( menuIds != null ) {
            sysRoleResponse.setMenuIds( Arrays.copyOf( menuIds, menuIds.length ) );
        }
        Long[] deptIds = entity.getDeptIds();
        if ( deptIds != null ) {
            sysRoleResponse.setDeptIds( Arrays.copyOf( deptIds, deptIds.length ) );
        }
        Set<String> set = entity.getPermissions();
        if ( set != null ) {
            sysRoleResponse.setPermissions( new LinkedHashSet<String>( set ) );
        }

        return sysRoleResponse;
    }

    @Override
    public List<SysRoleResponse> entityListToResponseList(List<SysRole> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<SysRoleResponse> list = new ArrayList<SysRoleResponse>( entityList.size() );
        for ( SysRole sysRole : entityList ) {
            list.add( entityToResponse( sysRole ) );
        }

        return list;
    }
}
