package com.abtk.product.service.system.impl;

import com.abtk.product.common.constant.UserConstants;
import com.abtk.product.common.exception.ServiceException;
import com.abtk.product.common.text.Convert;
import com.abtk.product.common.utils.SpringUtils;
import com.abtk.product.common.utils.StringUtils;

import com.abtk.product.dao.entity.SysDept;
import com.abtk.product.dao.entity.SysRole;
import com.abtk.product.dao.entity.SysUser;
import com.abtk.product.dao.mapper.SysDeptMapper;
import com.abtk.product.dao.mapper.SysRoleMapper;
import com.abtk.product.common.permission.annotation.DataScope;
import com.abtk.product.service.domain.vo.TreeSelect;
import com.abtk.product.service.security.utils.SecurityUtils;
import com.abtk.product.service.system.ISysDeptService;
import com.abtk.product.service.system.service.I18nService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 部门管理 服务实现
 * 
 * @author ruoyi
 */
@Service
public class SysDeptServiceImpl implements ISysDeptService
{
    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private I18nService i18nService;

    /**
     * 查询部门管理数据
     * 
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysDept> selectDeptList(SysDept dept)
    {
        return deptMapper.selectDeptList(dept);
    }

    /**
     * 查询部门树结构信息
     * 
     * @param dept 部门信息
     * @return 部门树信息集合
     */
    @Override
    public List<TreeSelect> selectDeptTreeList(SysDept dept)
    {
        List<SysDept> depts = SpringUtils.getAopProxy(this).selectDeptList(dept);
        return buildDeptTreeSelect(depts);
    }

    /**
     * 构建前端所需要树结构
     * 
     * @param depts 部门列表
     * @return 树结构列表
     */
    @Override
    public List<SysDept> buildDeptTree(List<SysDept> depts)
    {
        List<SysDept> returnList = new ArrayList<SysDept>();
        List<Long> tempList = depts.stream().map(SysDept::getId).filter(Objects::nonNull).collect(Collectors.toList());
        for (SysDept dept : depts)
        {
            Long parentId = null;
            try
            {
                parentId = Long.valueOf(dept.getParentId());
            }
            catch (NumberFormatException e)
            {
                parentId = null;
            }
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (dept.getId() == null || (parentId != null && !tempList.contains(parentId)))
            {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     * 
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts)
    {
        List<SysDept> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据角色ID查询部门树信息
     * 
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    @Override
    public List<String> selectDeptListByRoleId(Long roleId)
    {
        SysRole role = roleMapper.selectRoleById(roleId);
        return deptMapper.selectDeptListByRoleId(roleId, role.isDeptCheckStrictly());
    }

    /**
     * 根据部门ID查询信息
     * 
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Override
    public SysDept selectDeptById(String deptId)
    {
        return deptMapper.selectDeptById(deptId);
    }

    /**
     * 根据ID查询所有子部门（正常状态）
     * 
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Override
    public int selectNormalChildrenDeptById(String deptId)
    {
        return deptMapper.selectNormalChildrenDeptById(deptId);
    }

    /**
     * 是否存在子节点
     * 
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptId(String deptId)
    {
        int result = deptMapper.hasChildByDeptId(deptId);
        return result > 0;
    }

    /**
     * 查询部门是否存在用户
     * 
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(String deptId)
    {
        int result = deptMapper.checkDeptExistUser(deptId);
        return result > 0;
    }

    /**
     * 校验部门名称是否唯一
     * 
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public boolean checkDeptNameUnique(SysDept dept)
    {
        String deptId = StringUtils.isNull(dept.getDeptId()) ? "-1" : dept.getDeptId();
        SysDept info = deptMapper.checkDeptNameUnique(dept.getDeptName(), dept.getParentId());
        if (StringUtils.isNotNull(info) && !StringUtils.equals(info.getDeptId(), deptId))
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验部门是否有数据权限
     * 
     * @param deptId 部门id
     */
    @Override
    public void checkDeptDataScope(String deptId)
    {
        if (!SysUser.isAdmin(SecurityUtils.getUserId()) && StringUtils.isNotNull(deptId))
        {
            SysDept dept = new SysDept();
            dept.setDeptId(deptId);
            List<SysDept> depts = SpringUtils.getAopProxy(this).selectDeptList(dept);
            if (StringUtils.isEmpty(depts))
            {
                throw new ServiceException(i18nService.getMessage("permission.dept.access.denied"));
            }
        }
    }

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int insertDept(SysDept dept)
    {
        // parentId="0" 表示顶级部门，不需要处理父级
        if (!StringUtils.equals("0", dept.getParentId()))
        {
            SysDept info = deptMapper.selectDeptById(dept.getParentId());
            // 如果父节点不为正常状态,则不允许新增子节点
            if (!UserConstants.DEPT_NORMAL.equals(info.getStatus()))
            {
                throw new ServiceException(i18nService.getMessage("dept.disabled.not.allowed"));
            }
            dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
            // 自动计算部门全路径 = 上级部门全路径 + 部门名称
            dept.setDeptFullPath(StringUtils.isNotEmpty(info.getDeptFullPath())
                    ? info.getDeptFullPath() + "-" + dept.getDeptName()
                    : dept.getDeptName());
        }
        else
        {
            // 顶级部门，ancestors 初始为 "0"
            dept.setAncestors("0");
            // 顶级部门的全路径就是部门名称本身
            dept.setDeptFullPath(dept.getDeptName());
        }
        int rows = deptMapper.insertDept(dept);
        // useGeneratedKeys 会自动回填 deptId，更新 ancestors 追加自身ID
        if (rows > 0)
        {
            deptMapper.updateDeptAncestors(dept.getDeptId(), dept.getAncestors() + "," + dept.getDeptId());
        }
        return rows;
    }

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int updateDept(SysDept dept)
    {
        SysDept newParentDept = deptMapper.selectDeptById(dept.getParentId());
        SysDept oldDept = deptMapper.selectDeptById(dept.getDeptId());
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept))
        {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            // 重新计算部门全路径 = 上级部门全路径 + 部门名称
            dept.setDeptFullPath(StringUtils.isNotEmpty(newParentDept.getDeptFullPath())
                    ? newParentDept.getDeptFullPath() + dept.getDeptName()
                    : dept.getDeptName());
            updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors, dept.getDeptName());
        }
        else if (StringUtils.isNotNull(oldDept) && StringUtils.equals(oldDept.getParentId(), dept.getParentId()))
        {
            // 仅部门名称变更，无需修改上级部门，但仍需重新计算全路径
            SysDept parentDept = deptMapper.selectDeptById(dept.getParentId());
            dept.setAncestors(oldDept.getAncestors());
            dept.setDeptFullPath(parentDept != null && StringUtils.isNotEmpty(parentDept.getDeptFullPath())
                    ? parentDept.getDeptFullPath() + dept.getDeptName()
                    : dept.getDeptName());
            updateChildrenDeptFullPath(dept.getDeptId(), dept.getDeptName());
        }
        int result = deptMapper.updateDept(dept);
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus()) && StringUtils.isNotEmpty(dept.getAncestors())
                && !StringUtils.equals("0", dept.getAncestors()))
        {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatusNormal(dept);
        }
        return result;
    }

    /**
     * 修改部门状态
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int updateDeptStatus(SysDept dept)
    {
        return deptMapper.updateDeptStatus(dept);
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private void updateParentDeptStatusNormal(SysDept dept)
    {
        String ancestors = dept.getAncestors();
        String[] deptIds = ancestors.split(",");
        deptMapper.updateDeptStatusNormal(deptIds);
    }

    /**
     * 修改子元素关系
     *
     * @param deptId 被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     * @param parentName 父部门的新名称（用于更新子部门的全路径）
     */
    public void updateDeptChildren(String deptId, String newAncestors, String oldAncestors, String parentName)
    {
        List<SysDept> children = deptMapper.selectChildrenDeptById(deptId);
        for (SysDept child : children)
        {
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
            // 更新子部门的全路径：替换旧前缀为新前缀
            if (StringUtils.isNotEmpty(child.getDeptFullPath()) && StringUtils.isNotEmpty(oldAncestors))
            {
                // 获取子部门相对于当前父部门的名称（从旧全路径中提取）
                String oldParentPath = findDeptPathById(deptId);
                if (StringUtils.isNotEmpty(oldParentPath))
                {
                    child.setDeptFullPath(child.getDeptFullPath().replaceFirst(oldParentPath, parentName));
                }
            }
        }
        if (children.size() > 0)
        {
            deptMapper.updateDeptChildren(children);
            // 递归更新所有层级的子部门全路径
            for (SysDept child : children)
            {
                updateChildrenDeptFullPath(child.getDeptId(), child.getDeptName());
            }
        }
    }

    /**
     * 根据部门ID查找其全路径（不含自身名称）
     */
    private String findDeptPathById(String deptId)
    {
        if (deptId == null || StringUtils.equals("0", deptId))
        {
            return null;
        }
        SysDept dept = deptMapper.selectDeptById(deptId);
        if (dept == null || StringUtils.isEmpty(dept.getDeptFullPath()))
        {
            return null;
        }
        // 返回父路径（不含自身名称）
        String fullPath = dept.getDeptFullPath();
        String deptName = dept.getDeptName();
        if (fullPath.endsWith(deptName))
        {
            return fullPath.substring(0, fullPath.length() - deptName.length());
        }
        return fullPath;
    }

    /**
     * 递归更新子部门的全路径（当父部门名称变更时）
     */
    private void updateChildrenDeptFullPath(String parentId, String parentFullPath)
    {
        List<SysDept> children = deptMapper.selectChildrenDeptById(parentId);
        for (SysDept child : children)
        {
            if (StringUtils.isNotEmpty(child.getDeptFullPath()))
            {
                // 子部门的全路径 = 父部门的新全路径 + 子部门的名称
                child.setDeptFullPath(parentFullPath + child.getDeptName());
                deptMapper.updateDept(child);
                // 递归处理子部门的子部门
                updateChildrenDeptFullPath(child.getDeptId(), child.getDeptFullPath());
            }
        }
    }

    /**
     * 删除部门管理信息（包含递归删除无关联用户的子部门）
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public int deleteDeptById(String deptId)
    {
        int count = 0;
        // 递归删除无关联用户的子部门
        count += deleteChildrenWithoutUser(deptId);
        // 删除自身
        count += deptMapper.deleteDeptById(deptId);
        return count;
    }

    /**
     * 递归删除没有关联用户的子部门
     *
     * @param parentId 父部门ID
     * @return 删除的部门数量
     */
    private int deleteChildrenWithoutUser(String parentId)
    {
        int count = 0;
        List<SysDept> children = deptMapper.selectChildrenDeptById(parentId);
        for (SysDept child : children)
        {
            // 递归删除子部门的子部门
            count += deleteChildrenWithoutUser(child.getDeptId());
            // 如果子部门没有关联用户，则删除
            if (!checkDeptExistUser(child.getDeptId()))
            {
                count += deptMapper.deleteDeptById(child.getDeptId());
            }
        }
        return count;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysDept> list, SysDept t)
    {
        // 得到子节点列表
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysDept> getChildList(List<SysDept> list, SysDept t)
    {
        List<SysDept> tlist = new ArrayList<SysDept>();
        Iterator<SysDept> it = list.iterator();
        while (it.hasNext())
        {
            SysDept n = (SysDept) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && t.getId() != null
                    && StringUtils.equals(n.getParentId(), String.valueOf(t.getId())))
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDept> list, SysDept t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }
}
