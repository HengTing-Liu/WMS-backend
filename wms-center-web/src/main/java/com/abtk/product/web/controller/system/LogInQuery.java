package com.abtk.product.web.controller.system;

import com.abtk.product.common.web.domain.BaseEntity;

/**
 * 登录日志查询对象
 * 
 * @author wms
 */
public class LogInQuery extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户名 */
    private String userName;

    /** IP地址 */
    private String ipAddr;

    /** 登录状态 */
    private String status;

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getIpAddr()
    {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr)
    {
        this.ipAddr = ipAddr;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
