package com.abclonal.product.service.dict.impl;

import com.abclonal.product.dao.entity.WmsDict;
import com.abclonal.product.dao.mapper.WmsDictMapper;
import com.abclonal.product.service.dict.service.WmsDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 字典管理表(WmsDict)表服务实现类
 *
 * @author backend
 * @since 2026-03-26
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WmsDictServiceImpl implements WmsDictService {

    @Autowired
    private WmsDictMapper wmsDictMapper;

    // ==================== 查询方法 ====================

    @Override
    public WmsDict queryById(Long id) {
        return wmsDictMapper.queryById(id);
    }

    @Override
    public List<WmsDict> queryByCondition(WmsDict wmsDict) {
        return wmsDictMapper.queryAll(wmsDict);
    }

    @Override
    public List<WmsDict> listAll() {
        return wmsDictMapper.listAll();
    }

    @Override
    public long count(WmsDict wmsDict) {
        return wmsDictMapper.count(wmsDict);
    }

    @Override
    public boolean checkDictCodeUnique(String dictCode) {
        return wmsDictMapper.checkDictCodeUnique(dictCode) == null;
    }

    // ==================== 新增方法 ====================

    @Override
    public WmsDict insert(WmsDict wmsDict) {
        Date now = new Date();
        if (wmsDict.getCreateTime() == null) {
            wmsDict.setCreateTime(now);
        }
        if (wmsDict.getUpdateTime() == null) {
            wmsDict.setUpdateTime(now);
        }
        if (wmsDict.getIsDeleted() == null) {
            wmsDict.setIsDeleted(0);
        }
        if (wmsDict.getIsEnabled() == null) {
            wmsDict.setIsEnabled(1);
        }
        wmsDictMapper.insert(wmsDict);
        return wmsDict;
    }

    // ==================== 修改方法 ====================

    @Override
    public int update(WmsDict wmsDict) {
        wmsDict.setUpdateTime(new Date());
        return wmsDictMapper.update(wmsDict);
    }

    // ==================== 删除方法 ====================

    @Override
    public boolean logicDeleteById(Long id, String username) {
        return wmsDictMapper.deleteById(id, username) > 0;
    }

    @Override
    public void toggleStatus(Long id, Integer isEnabled) {
        wmsDictMapper.toggleStatus(id, isEnabled);
    }
}
