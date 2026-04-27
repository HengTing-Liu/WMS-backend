package com.abtk.product.dao.mapper;

import com.abtk.product.dao.entity.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门管理 数据层
 * 
 * @author ruoyi
 */
public interface SysDeptMapper
{
    /**
     * 查询部门管理数据
     * 
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<SysDept> selectDeptList(SysDept dept);

    /**
     * 根据角色ID查询部门树信息
     * 
     * @param roleId 角色ID
     * @param deptCheckStrictly 部门树选择项是否关联显示
     * @return 选中部门列表
     */
    public List<String> selectDeptListByRoleId(@Param("roleId") Long roleId, @Param("deptCheckStrictly") boolean deptCheckStrictly);

    /**
     * 根据部门编码查询信息
     * 
     * @param deptId 部门编码
     * @return 部门信息
     */
    public SysDept selectDeptById(String deptId);

    /**
     * 根据编码查询所有子部门
     * 
     * @param deptId 部门编码
     * @return 部门列表
     */
    public List<SysDept> selectChildrenDeptById(String deptId);

    /**
     * 根据编码查询所有子部门（正常状态）
     * 
     * @param deptId 部门编码
     * @return 子部门数
     */
    public int selectNormalChildrenDeptById(String deptId);

    /**
     * 是否存在子节点
     * 
     * @param deptId 部门编码
     * @return 结果
     */
    public int hasChildByDeptId(String deptId);

    /**
     * 查询部门是否存在用户
     * 
     * @param deptId 部门编码
     * @return 结果
     */
    public int checkDeptExistUser(String deptId);

    /**
     * 校验部门名称是否唯一
     * 
     * @param deptName 部门名称
     * @param parentId 父部门编码
     * @return 结果
     */
    public SysDept checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") String parentId);

    /**
     * 新增部门信息
     * 
     * @param dept 部门信息
     * @return 结果
     */
    public int insertDept(SysDept dept);

    /**
     * 修改部门信息
     * 
     * @param dept 部门信息
     * @return 结果
     */
    public int updateDept(SysDept dept);

    /**
     * 修改部门状态
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int updateDeptStatus(SysDept dept);

    /**
     * 修改所在部门正常状态
     * 
     * @param deptIds 部门编码组
     */
    public void updateDeptStatusNormal(String[] deptIds);

    /**
     * 修改子元素关系
     * 
     * @param depts 子元素
     * @return 结果
     */
    public int updateDeptChildren(@Param("depts") List<SysDept> depts);

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门编码
     * @return 结果
     */
    public int deleteDeptById(String deptId);

    /**
     * 获取最近一次插入的部门编码
     *
     * @return 最近插入的编码
     */
    public String selectLastInsertId();

    /**
     * 更新部门祖籍列表
     *
     * @param deptId 部门编码
     * @param ancestors 祖籍列表
     * @return 结果
     */
    public int updateDeptAncestors(@Param("deptId") String deptId, @Param("ancestors") String ancestors);
}
