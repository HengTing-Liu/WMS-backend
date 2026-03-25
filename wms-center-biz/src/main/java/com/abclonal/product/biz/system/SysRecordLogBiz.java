package com.abclonal.product.biz.system;


import com.abclonal.product.common.constant.Constants;
import com.abclonal.product.common.utils.StringUtils;
import com.abclonal.product.common.utils.ip.IpUtils;
import com.abclonal.product.dao.entity.SysLogininfor;
import com.abclonal.product.service.system.ISysLogininforService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 记录日志方法
 * 
 * @author ruoyi
 */
@Component
public class SysRecordLogBiz
{
//    @Autowired
//    private RemoteLogService remoteLogService;
    @Autowired
    private ISysLogininforService sysLogininforService;

    /**
     * 记录登录信息
     * 
     * @param username 用户名
     * @param status 状态
     * @param message 消息内容
     * @return
     */
    public void recordLogininfor(String username, String status, String message)
    {
        SysLogininfor logininfor = new SysLogininfor();
        logininfor.setUserName(username);
        logininfor.setIpaddr(IpUtils.getIpAddr());
        logininfor.setMsg(message);
        // 日志状态
        if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER))
        {
            logininfor.setStatus(Constants.LOGIN_SUCCESS_STATUS);
        }
        else if (Constants.LOGIN_FAIL.equals(status))
        {
            logininfor.setStatus(Constants.LOGIN_FAIL_STATUS);
        }
        sysLogininforService.insertLogininfor(logininfor);
//        return toAjax(logininforService.insertLogininfor(logininfor));

//        remoteLogService.saveLogininfor(logininfor, SecurityConstants.INNER);
    }
}
