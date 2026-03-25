package com.abclonal.product.service.inv.impl;
import org.springframework.beans.factory.annotation.Autowired;
import com.abclonal.product.dao.entity.InvQrcodedetail;
import com.abclonal.product.dao.mapper.InvQrcodedetailMapper;
import com.abclonal.product.service.inv.service.InvQrcodedetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * (InvQrcodedetail)表服务实现类
 *
 * @author lht
 * @since 2026-03-02 13:45:03
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class InvQrcodedetailServiceImpl implements InvQrcodedetailService {

     @Autowired
    private InvQrcodedetailMapper invQrcodedetailMapper;

    // ==================== 查询方法 ====================
    
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public InvQrcodedetail queryById(Long id) {
        return invQrcodedetailMapper.queryById(id);
    }

    /**
     * 根据条件查询列表
     *
     * @param invQrcodedetail 筛选条件
     * @return 对象列表
     */
    @Override
    public List<InvQrcodedetail> queryByCondition(InvQrcodedetail invQrcodedetail) {
        return invQrcodedetailMapper.queryAll(invQrcodedetail);
    }

    /**
     * 统计总行数
     *
     * @param invQrcodedetail 筛选条件
     * @return 总行数
     */
    @Override
    public long count(InvQrcodedetail invQrcodedetail) {
        return invQrcodedetailMapper.count(invQrcodedetail);
    }

    /**
     * 判断数据是否存在
     *
     * @param id 主键
     * @return 是否存在
     */
    @Override
    public boolean existsById(Long id) {
        return invQrcodedetailMapper.queryById(id) != null;
    }

    // ==================== 新增方法 ====================

    /**
     * 新增单条数据
     *
     * @param invQrcodedetail 实例对象
     * @return 实例对象
     */
    @Override
    public InvQrcodedetail insert(InvQrcodedetail invQrcodedetail) {
        invQrcodedetailMapper.insert(invQrcodedetail);
        return invQrcodedetail;
    }

    @Override
    public int insertBatchAtomic(List<InvQrcodedetail> entities) {
        return invQrcodedetailMapper.insertBatch(entities);
    }

    /**
     * 批量新增或更新
     *
     * @param entities 实例对象列表
     * @return 影响行数
     */
    @Override
    public int insertOrUpdateBatch(List<InvQrcodedetail> entities) {
        if (entities == null || entities.isEmpty()) {
            return 0;
        }
        return invQrcodedetailMapper.insertOrUpdateBatch(entities);
    }

    // ==================== 修改方法 ====================

    /**
     * 修改数据
     *
     * @param invQrcodedetail 实例对象
     * @return 实例对象
     */
    @Override
    public int update(InvQrcodedetail invQrcodedetail) {
        invQrcodedetail.setUpdateTime(new java.util.Date());
        return  invQrcodedetailMapper.update(invQrcodedetail);
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
        return invQrcodedetailMapper.deleteById(id,username) > 0;
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
        return invQrcodedetailMapper.deleteBatchByIds(ids,username);
    }


    @Override
    public int updateBatchAtomic(List<InvQrcodedetail> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return invQrcodedetailMapper.updateBatch(list);
    }

}
