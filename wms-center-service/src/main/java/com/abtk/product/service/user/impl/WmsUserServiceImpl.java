package com.abtk.product.service.user.impl;

import com.abtk.product.dao.entity.WmsUser;
import com.abtk.product.dao.mapper.user.WmsUserMapper;
import com.abtk.product.service.user.service.WmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 用户管理表(WmsUser)表服务实现类
 *
 * @author backend
 * @since 2026-03-26
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WmsUserServiceImpl implements WmsUserService {

    @Autowired
    private WmsUserMapper wmsUserMapper;

    // ==================== 查询方法 ====================

    @Override
    public WmsUser queryById(Long userId) {
        return wmsUserMapper.queryById(userId);
    }

    @Override
    public List<WmsUser> queryByCondition(WmsUser wmsUser) {
        return wmsUserMapper.queryAll(wmsUser);
    }

    @Override
    public List<WmsUser> listAll() {
        return wmsUserMapper.listAll();
    }

    @Override
    public long count(WmsUser wmsUser) {
        return wmsUserMapper.count(wmsUser);
    }

    @Override
    public boolean checkUserNameUnique(String userName) {
        return wmsUserMapper.checkUserNameUnique(userName) == null;
    }

    // ==================== 新增方法 ====================

    @Override
    public WmsUser insert(WmsUser wmsUser) {
        Date now = new Date();
        if (wmsUser.getCreateTime() == null) {
            wmsUser.setCreateTime(now);
        }
        if (wmsUser.getUpdateTime() == null) {
            wmsUser.setUpdateTime(now);
        }
        if (wmsUser.getDelFlag() == null) {
            wmsUser.setDelFlag("0");
        }
        if (wmsUser.getStatus() == null) {
            wmsUser.setStatus("0");
        }
        wmsUserMapper.insert(wmsUser);
        return wmsUser;
    }

    // ==================== 修改方法 ====================

    @Override
    public int update(WmsUser wmsUser) {
        wmsUser.setUpdateTime(new Date());
        return wmsUserMapper.update(wmsUser);
    }

    // ==================== 删除方法 ====================

    @Override
    public boolean logicDeleteById(Long userId, String username) {
        return wmsUserMapper.deleteById(userId, username) > 0;
    }

    @Override
    public void toggleStatus(Long userId, String status) {
        wmsUserMapper.toggleStatus(userId, status);
    }
}
