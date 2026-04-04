package com.abtk.product.service.system.impl;
import org.springframework.beans.factory.annotation.Autowired;
import com.abtk.product.dao.entity.SysEnumItem;
import com.abtk.product.dao.mapper.SysEnumItemMapper;
import com.abtk.product.service.system.service.SysEnumItemService;
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
 * 枚举明细表(SysEnumItem)表服务实现类
 *
 * @author lht
 * @since 2026-03-06 16:21:51
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysEnumItemServiceImpl implements SysEnumItemService {

     @Autowired
    private SysEnumItemMapper sysEnumItemMapper;

    // ==================== 查询方法 ====================
    
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SysEnumItem queryById(Long id) {
        return sysEnumItemMapper.queryById(id);
    }

    /**
     * 根据条件查询列表
     *
     * @param sysEnumItem 筛选条件
     * @return 对象列表
     */
    @Override
    public List<SysEnumItem> queryByCondition(SysEnumItem sysEnumItem) {
        return sysEnumItemMapper.queryAll(sysEnumItem);
    }

    /**
     * 统计总行数
     *
     * @param sysEnumItem 筛选条件
     * @return 总行数
     */
    @Override
    public long count(SysEnumItem sysEnumItem) {
        return sysEnumItemMapper.count(sysEnumItem);
    }

    /**
     * 判断数据是否存在
     *
     * @param id 主键
     * @return 是否存在
     */
    @Override
    public boolean existsById(Long id) {
        return sysEnumItemMapper.queryById(id) != null;
    }

    // ==================== 新增方法 ====================

    /**
     * 新增单条数据
     *
     * @param sysEnumItem 实例对象
     * @return 实例对象
     */
    @Override
    public SysEnumItem insert(SysEnumItem sysEnumItem) {
        sysEnumItemMapper.insert(sysEnumItem);
        return sysEnumItem;
    }
    
    @Override
    public int insertBatchAtomic(List<SysEnumItem> entities) {
        return sysEnumItemMapper.insertBatch(entities);
    }

    /**
     * 批量新增或更新
     *
     * @param entities 实例对象列表
     * @return 影响行数
     */
    @Override
    public int insertOrUpdateBatch(List<SysEnumItem> entities) {
        if (entities == null || entities.isEmpty()) {
            return 0;
        }
        return sysEnumItemMapper.insertOrUpdateBatch(entities);
    }

    // ==================== 修改方法 ====================

    /**
     * 修改数据
     *
     * @param sysEnumItem 实例对象
     * @return 实例对象
     */
    @Override
    public int update(SysEnumItem sysEnumItem) {
        return   sysEnumItemMapper.update(sysEnumItem);
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
        return sysEnumItemMapper.deleteById(id,username) > 0;
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
        return sysEnumItemMapper.deleteBatchByIds(ids,username);
    }
    
    @Override
    public int updateBatchAtomic(List<SysEnumItem> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return sysEnumItemMapper.updateBatch(list);
    }
}
