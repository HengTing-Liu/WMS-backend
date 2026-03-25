package com.abclonal.product.service.system.impl;
import org.springframework.beans.factory.annotation.Autowired;
import com.abclonal.product.dao.entity.SysEnumDefine;
import com.abclonal.product.dao.entity.SysEnumItem;
import com.abclonal.product.dao.mapper.SysEnumDefineCustomMapper;
import com.abclonal.product.dao.mapper.SysEnumDefineMapper;
import com.abclonal.product.service.system.service.SysEnumDefineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.annotation.Resource;
import java.util.List;
import java.util.Collections;

/**
 * 枚举定义表(SysEnumDefine)表服务实现类
 *
 * @author lht
 * @since 2026-03-06 15:20:34
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysEnumDefineServiceImpl implements SysEnumDefineService {

     @Autowired
    private SysEnumDefineMapper sysEnumDefineMapper;

    @Autowired
    private SysEnumDefineCustomMapper sysEnumDefineCustomMapper;

    // ==================== 查询方法 ====================
    
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SysEnumDefine queryById(Long id) {
        return sysEnumDefineMapper.queryById(id);
    }

    /**
     * 根据条件查询列表
     *
     * @param sysEnumDefine 筛选条件
     * @return 对象列表
     */
    @Override
    public List<SysEnumDefine> queryByCondition(SysEnumDefine sysEnumDefine) {
        return sysEnumDefineMapper.queryAll(sysEnumDefine);
    }

    /**
     * 统计总行数
     *
     * @param sysEnumDefine 筛选条件
     * @return 总行数
     */
    @Override
    public long count(SysEnumDefine sysEnumDefine) {
        return sysEnumDefineMapper.count(sysEnumDefine);
    }

    /**
     * 判断数据是否存在
     *
     * @param id 主键
     * @return 是否存在
     */
    @Override
    public boolean existsById(Long id) {
        return sysEnumDefineMapper.queryById(id) != null;
    }

    // ==================== 新增方法 ====================

    /**
     * 新增单条数据
     *
     * @param sysEnumDefine 实例对象
     * @return 实例对象
     */
    @Override
    public SysEnumDefine insert(SysEnumDefine sysEnumDefine) {
        sysEnumDefineMapper.insert(sysEnumDefine);
        return sysEnumDefine;
    }
    
    @Override
    public int insertBatchAtomic(List<SysEnumDefine> entities) {
        return sysEnumDefineMapper.insertBatch(entities);
    }

    /**
     * 批量新增或更新
     *
     * @param entities 实例对象列表
     * @return 影响行数
     */
    @Override
    public int insertOrUpdateBatch(List<SysEnumDefine> entities) {
        if (entities == null || entities.isEmpty()) {
            return 0;
        }
        return sysEnumDefineMapper.insertOrUpdateBatch(entities);
    }

    // ==================== 修改方法 ====================

    /**
     * 修改数据
     *
     * @param sysEnumDefine 实例对象
     * @return 实例对象
     */
    @Override
    public int update(SysEnumDefine sysEnumDefine) {
        // 设置更新时间和更新人（如果未设置）
        if (sysEnumDefine.getUpdateTime() == null) {
            sysEnumDefine.setUpdateTime(new java.util.Date());
        }
        return sysEnumDefineMapper.update(sysEnumDefine);
    }

    // ==================== 删除方法 ====================

    /**
     * 通过主键逻辑删除数据（假删除）
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean logicDeleteById(Long id,String username) {
        return sysEnumDefineMapper.deleteById(id,username) > 0;
    }
    
    /**
     * 批量逻辑删除数据（假删除）
     *
     * @param ids 主键列表
     * @return 影响行数
     */
    @Override
    public int logicDeleteBatchByIds(List<Long> ids,String username) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        return sysEnumDefineMapper.deleteBatchByIds(ids,username);
    }
    
    @Override
    public int updateBatchAtomic(List<SysEnumDefine> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return sysEnumDefineMapper.updateBatch(list);
    }

    // ==================== 复合操作方法 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addEnumDefineWithItems(SysEnumDefine enumDefine, List<SysEnumItem> enumItems) {
        // 1. 插入枚举定义
        sysEnumDefineCustomMapper.insertEnumDefine(enumDefine);

        // 2. 获取插入后的ID
        Long enumDefineId = enumDefine.getId();

        // 3. 如果有明细数据，设置枚举编码并批量插入
        if (enumItems != null && !enumItems.isEmpty()) {
            String enumCode = enumDefine.getEnumCode();
            for (SysEnumItem item : enumItems) {
                item.setEnumCode(enumCode);
            }
            sysEnumDefineCustomMapper.batchInsertEnumItems(enumItems);
        }

        return enumDefineId;
    }

}
