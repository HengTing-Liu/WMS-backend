package com.abclonal.product.service.system.impl;

import com.abclonal.product.dao.entity.LogIn;
import com.abclonal.product.dao.mapper.LogInMapper;
import com.abclonal.product.service.system.ILogInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 登录日志 服务层处理
 * 
 * @author wms
 */
@Service
public class LogInServiceImpl implements ILogInService
{

    @Autowired
    private LogInMapper logInMapper;

    /**
     * 查询登录日志列表
     * 
     * @param logIn 登录日志对象
     * @return 登录日志集合
     */
    @Override
    public List<LogIn> selectLogInList(LogIn logIn)
    {
        return logInMapper.selectLogInList(logIn);
    }
}
