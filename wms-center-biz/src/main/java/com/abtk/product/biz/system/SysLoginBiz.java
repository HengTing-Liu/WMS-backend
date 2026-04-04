package com.abtk.product.biz.system;


import com.abtk.product.common.constant.CacheConstants;
import com.abtk.product.common.constant.Constants;
import com.abtk.product.common.constant.UserConstants;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.enums.UserStatus;
import com.abtk.product.common.exception.ServiceException;
import com.abtk.product.common.text.Convert;
import com.abtk.product.common.utils.DateUtils;
import com.abtk.product.common.utils.StringUtils;
import com.abtk.product.common.utils.ip.IpUtils;
import com.abtk.product.dao.entity.SysUser;
import com.abtk.product.service.domain.LoginUser;
import com.abtk.product.service.redis.service.RedisService;
import com.abtk.product.service.security.utils.SecurityUtils;
import com.abtk.product.service.system.SysRecordLogService;
import com.abtk.product.service.system.service.I18nService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 登录校验方法
 * 
 * @author ruoyi
 */
@Component
public class SysLoginBiz
{
    @Autowired
    private SysUserBiz  userBiz;

    @Autowired
    private SysPasswordBiz passwordService;

    @Autowired
    private SysRecordLogService recordLogService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private I18nService i18nService;

    /**
     * 登录
     */
    public LoginUser login(String username, String password)
    {
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(username, password))
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, i18nService.getMessage("user.name.required"));
            throw new ServiceException(i18nService.getMessage("user.name.required"));
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, i18nService.getMessage("user.password.length"));
            throw new ServiceException(i18nService.getMessage("user.password.length"));
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, i18nService.getMessage("user.name.length"));
            throw new ServiceException(i18nService.getMessage("user.name.length"));
        }
        // IP黑名单校验
        String blackStr = Convert.toStr(redisService.getCacheObject(CacheConstants.SYS_LOGIN_BLACKIPLIST));
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr()))
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, i18nService.getMessage("auth.ip.blacklist"));
            throw new ServiceException(i18nService.getMessage("auth.ip.blacklist"));
        }
        // 查询用户信息
//        R<LoginUser> userResult = remoteUserService.getUserInfo(username, SecurityConstants.INNER);
        R<LoginUser> userResult = userBiz.getUserInfo(username);

        if (R.FAIL == userResult.getCode())
        {
            throw new ServiceException(userResult.getMsg());
        }

        LoginUser userInfo = userResult.getData();
        SysUser user = userResult.getData().getSysUser();
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, i18nService.getMessage("user.deleted"));
            throw new ServiceException(i18nService.getMessage("user.deleted", username));
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, i18nService.getMessage("user.disabled"));
            throw new ServiceException(i18nService.getMessage("user.disabled", username));
        }
        passwordService.validate(user, password);
        recordLogService.recordLogininfor(username, Constants.LOGIN_SUCCESS, i18nService.getMessage("login.success"));
        recordLoginInfo(user.getUserId());
        return userInfo;
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId)
    {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        // 更新用户登录IP
        sysUser.setLoginIp(IpUtils.getIpAddr());
        // 更新用户登录时间
        sysUser.setLoginDate(DateUtils.getNowDate());
//        remoteUserService.recordUserLogin(sysUser, SecurityConstants.INNER);
        userBiz.recordUserLogin(sysUser);
    }

    public void logout(String loginName)
    {
        recordLogService.recordLogininfor(loginName, Constants.LOGOUT, i18nService.getMessage("logout.success"));
    }

    /**
     * 注册
     */
    public void register(String username, String password)
    {
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(username, password))
        {
            throw new ServiceException(i18nService.getMessage("user.name.required"));
        }
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            throw new ServiceException(i18nService.getMessage("user.name.length"));
        }
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            throw new ServiceException(i18nService.getMessage("user.password.length"));
        }

        // 注册用户信息
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setNickName(username);
        sysUser.setPwdUpdateDate(DateUtils.getNowDate());
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        R<Boolean> registerResult = userBiz.registerUserInfo(sysUser);
        if (R.FAIL == registerResult.getCode())
        {
            throw new ServiceException(registerResult.getMsg());
        }
        recordLogService.recordLogininfor(username, Constants.REGISTER, i18nService.getMessage("user.register.success"));
    }
}
