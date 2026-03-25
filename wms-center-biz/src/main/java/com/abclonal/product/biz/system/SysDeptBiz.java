package com.abclonal.product.biz.system;

import com.abclonal.product.common.exception.ServiceException;
import com.abclonal.product.common.utils.StringUtils;
import com.abclonal.product.dao.entity.SysDept;
import com.abclonal.product.dao.entity.SysUser;
import com.abclonal.product.service.domain.vo.TreeSelect;
import com.abclonal.product.service.security.utils.SecurityUtils;
import com.abclonal.product.service.system.ISysDeptService;
import com.abclonal.product.service.system.service.I18nService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 *
 * @description:
 * @author: 75618
 * @time: 2026/2/5 13:24
 *
 */
@Component
public class SysDeptBiz {
    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private I18nService i18nService;

    public List<TreeSelect> selectDeptTreeList(SysDept dept)
    {
        // 默认只查询正常状态的部门
        if (StringUtils.isEmpty(dept.getStatus())) {
            dept.setStatus("0");
        }
        List<SysDept> depts = sysDeptService.selectDeptList(dept);
        return sysDeptService.buildDeptTreeSelect(depts);
    }

    public void checkDeptDataScope(Long deptId){

        if (!SysUser.isAdmin(SecurityUtils.getUserId()) && StringUtils.isNotNull(deptId))
        {
            SysDept dept = new SysDept();
            dept.setDeptId(deptId);
            List<SysDept> depts = sysDeptService.selectDeptList(dept);
            if (StringUtils.isEmpty(depts))
            {
                throw new ServiceException(i18nService.getMessage("permission.dept.access.denied"));
            }
        }
    }
}
