package com.abtk.product.dao.gengrator.mapper.permission;

import com.abtk.product.dao.gengrator.entity.permission.CompanyDataPermission;

import java.util.List;

/**
 * 公司数据权限 数据层
 *
 * @author backend1
 */
public interface CompanyDataPermissionMapper {

    /**
     * 查询公司数据权限信息
     *
     * @param id 公司数据权限主键
     * @return 公司数据权限信息
     */
    public CompanyDataPermission selectCompanyDataPermissionById(Long id);

    /**
    * 根据公司ID和表编码查询公司数据权限
     *
     * @param companyId 公司ID
     * @param tableCode 表编码
     * @return 公司数据权限信息
     */
    public CompanyDataPermission selectByCompanyIdAndTableCode(Long companyId, String tableCode);

    /**
     * 查询公司数据权限列表
     *
     * @param companyDataPermission 公司数据权限信息
     * @return 公司数据权限集合
     */
    public List<CompanyDataPermission> selectCompanyDataPermissionList(CompanyDataPermission companyDataPermission);

    /**
     * 根据公司ID查询公司数据权限列表
     *
     * @param companyId 公司ID
     * @return 公司数据权限集合
     */
    public List<CompanyDataPermission> selectByCompanyId(Long companyId);

    /**
     * 新增公司数据权限
     *
     * @param companyDataPermission 公司数据权限信息
     * @return 结果
     */
    public int insertCompanyDataPermission(CompanyDataPermission companyDataPermission);

    /**
     * 修改公司数据权限
     *
     * @param companyDataPermission 公司数据权限信息
     * @return 结果
     */
    public int updateCompanyDataPermission(CompanyDataPermission companyDataPermission);

    /**
     * 删除公司数据权限
     *
     * @param id 公司数据权限主键
     * @return 结果
     */
    public int deleteCompanyDataPermissionById(Long id);

    /**
     * 批量删除公司数据权限
     *
     * @param ids 需要删除的公司数据权限主键集合
     * @return 结果
     */
    public int deleteCompanyDataPermissionByIds(Long[] ids);

    /**
     * 根据公司ID和表编码删除公司数据权限
     *
     * @param companyId 公司ID
     * @param tableCode 表编码
     * @return 结果
     */
    public int deleteByCompanyIdAndTableCode(Long companyId, String tableCode);

    /**
     * 逻辑删除公司数据权限
     *
     * @param id 公司数据权限主键
     * @return 结果
     */
    public int deleteLogicById(Long id);
}
