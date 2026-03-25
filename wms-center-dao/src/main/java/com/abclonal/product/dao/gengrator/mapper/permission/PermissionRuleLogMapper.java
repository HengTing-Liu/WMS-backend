package com.abclonal.product.dao.gengrator.mapper.permission;

import com.abclonal.product.dao.gengrator.entity.permission.PermissionRuleLog;

import java.util.List;

/**
 * 权限规则日志 数据层
 *
 * @author backend1
 */
public interface PermissionRuleLogMapper {

    /**
     * 查询权限规则日志信息
     *
     * @param id 权限规则日志主键
     * @return 权限规则日志信息
     */
    public PermissionRuleLog selectPermissionRuleLogById(Long id);

    /**
     * 查询权限规则日志列表
     *
     * @param permissionRuleLog 权限规则日志信息
     * @return 权限规则日志集合
     */
    public List<PermissionRuleLog> selectPermissionRuleLogList(PermissionRuleLog permissionRuleLog);

    /**
     * 根据表编码查询权限规则日志列表
     *
     * @param tableCode 表编码
     * @return 权限规则日志集合
     */
    public List<PermissionRuleLog> selectByTableCode(String tableCode);

    /**
     * 根据维度ID查询权限规则日志列表
     *
     * @param permissionDimension 权限维度
     * @param dimensionId        维度ID
     * @return 权限规则日志集合
     */
    public List<PermissionRuleLog> selectByDimensionId(String permissionDimension, Long dimensionId);

    /**
     * 新增权限规则日志
     *
     * @param permissionRuleLog 权限规则日志信息
     * @return 结果
     */
    public int insertPermissionRuleLog(PermissionRuleLog permissionRuleLog);

    /**
     * 批量新增权限规则日志
     *
     * @param permissionRuleLogs 权限规则日志集合
     * @return 结果
     */
    public int batchInsertPermissionRuleLog(List<PermissionRuleLog> permissionRuleLogs);

    /**
     * 删除权限规则日志
     *
     * @param id 权限规则日志主键
     * @return 结果
     */
    public int deletePermissionRuleLogById(Long id);

    /**
     * 批量删除权限规则日志
     *
     * @param ids 需要删除的权限规则日志主键集合
     * @return 结果
     */
    public int deletePermissionRuleLogByIds(Long[] ids);

    /**
     * 逻辑删除权限规则日志
     *
     * @param id 权限规则日志主键
     * @return 结果
     */
    public int deleteLogicById(Long id);

    /**
     * 清空指定时间之前的日志
     *
     * @param beforeTime 时间
     * @return 结果
     */
    public int deleteByBeforeTime(java.util.Date beforeTime);
}
