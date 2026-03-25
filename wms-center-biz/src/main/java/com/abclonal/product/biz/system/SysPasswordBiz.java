package com.abclonal.product.biz.system;


import com.abclonal.product.common.constant.CacheConstants;
import com.abclonal.product.common.constant.Constants;
import com.abclonal.product.common.exception.ServiceException;
import com.abclonal.product.dao.entity.SysUser;
import com.abclonal.product.service.redis.service.RedisService;
import com.abclonal.product.service.security.utils.SecurityUtils;
import com.abclonal.product.service.system.SysRecordLogService;
import com.abclonal.product.service.system.service.I18nService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 登录密码方法
 * 
 * @author ruoyi
 */
@Component
public class SysPasswordBiz
{
    @Autowired
    private RedisService redisService;

    @Autowired
    private SysRecordLogService recordLogService;

    @Autowired
    private I18nService i18nService;

    private int maxRetryCount = CacheConstants.PASSWORD_MAX_RETRY_COUNT;

    private Long lockTime = CacheConstants.PASSWORD_LOCK_TIME;

    /**
     * 登录账户密码错误次数缓存键名
     * 
     * @param username 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String username)
    {
        return CacheConstants.PWD_ERR_CNT_KEY + username;
    }

    public void validate(SysUser user, String password)
    {
        String username = user.getUserName();

        Integer retryCount = redisService.getCacheObject(getCacheKey(username));

        if (retryCount == null)
        {
            retryCount = 0;
        }

        if (retryCount >= Integer.valueOf(maxRetryCount).intValue())
        {
            String errMsg = i18nService.getMessage("password.retry.exceed", maxRetryCount, lockTime);
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, errMsg);
            throw new ServiceException(errMsg);
        }

        if (!matches(user, password))
        {
            retryCount = retryCount + 1;
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, i18nService.getMessage("password.retry.count", retryCount));
            redisService.setCacheObject(getCacheKey(username), retryCount, lockTime, TimeUnit.MINUTES);
            throw new ServiceException(i18nService.getMessage("user.password.error"));
        }
        else
        {
            clearLoginRecordCache(username);
        }
    }

    public boolean matches(SysUser user, String rawPassword)
    {
        return SecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    public void clearLoginRecordCache(String loginName)
    {
        if (redisService.hasKey(getCacheKey(loginName)))
        {
            redisService.deleteObject(getCacheKey(loginName));
        }
    }
}
