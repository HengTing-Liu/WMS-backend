package com.abtk.product.biz.system;

import com.abtk.product.api.domain.request.sys.SysRoleRequest;
import com.abtk.product.api.domain.response.sys.SysRoleResponse;
import com.abtk.product.common.exception.ServiceException;
import com.abtk.product.dao.entity.SysUser;
import com.abtk.product.service.security.utils.SecurityUtils;
import com.abtk.product.service.system.service.I18nService;
import com.abtk.product.service.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 *
 * @description:
 * @author: 75618
 * @time: 2026/2/5 16:14
 *
 */
@Component
public class SysRoleBiz {

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private I18nService i18nService;

    public void checkRoleDataScope(Long... roleIds)
    {
        if (!SysUser.isAdmin(SecurityUtils.getUserId()))
        {
            for (Long roleId : roleIds)
            {
                SysRoleRequest request = new SysRoleRequest();
                request.setRoleId(roleId);
                List<SysRoleResponse> roles = sysRoleService.selectRoleList(request);
                if (roles == null || roles.isEmpty())
                {
                    throw new ServiceException(i18nService.getMessage("permission.role.access.denied"));
                }
            }
        }
    }
}
