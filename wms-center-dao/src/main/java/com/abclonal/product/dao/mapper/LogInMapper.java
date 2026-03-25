package com.abclonal.product.dao.mapper;

import com.abclonal.product.dao.entity.LogIn;

import java.util.List;

/**
 * 登录日志 数据层
 * 
 * @author wms
 */
public interface LogInMapper
{
    /**
     * 查询登录日志列表
     * 
     * @param logIn 登录日志对象
     * @return 登录日志集合
     */
    public List<LogIn> selectLogInList(LogIn logIn);
}
