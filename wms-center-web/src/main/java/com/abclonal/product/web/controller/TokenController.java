package com.abclonal.product.web.controller;

import com.abclonal.product.api.domain.request.LoginBodyRequest;
import com.abclonal.product.api.domain.request.RegisterBodyRequest;
import com.abclonal.product.biz.system.SysLoginBiz;
import com.abclonal.product.common.domain.R;
import com.abclonal.product.common.utils.JwtUtils;
import com.abclonal.product.common.utils.StringUtils;
import com.abclonal.product.service.domain.LoginUser;
import com.abclonal.product.service.security.TokenService;
import com.abclonal.product.service.security.utils.SecurityUtils;
import com.abclonal.product.web.security.auth.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * token 控制
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/api")
public class TokenController
{
    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginBiz sysLoginBiz;
//    private SysLoginService sysLoginService;

    @PostMapping("/login")
    public R<?> login(@RequestBody LoginBodyRequest form)
    {
        // 用户登录
        LoginUser userInfo = sysLoginBiz.login(form.getUsername(), form.getPassword());
        // 获取登录token
        return R.ok(tokenService.createToken(userInfo));
    }

    @DeleteMapping("logout")
    public R<?> logout(HttpServletRequest request)
    {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotEmpty(token))
        {
            String username = JwtUtils.getUserName(token);
            // 删除用户缓存记录
            AuthUtil.logoutByToken(token);
            // 记录用户退出日志
            sysLoginBiz.logout(username);
        }
        return R.ok();
    }

    @PostMapping("refresh")
    public R<?> refresh(HttpServletRequest request)
    {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser))
        {
            // 刷新令牌有效期
            tokenService.refreshToken(loginUser);
            return R.ok();
        }
        return R.ok();
    }

    @PostMapping("register")
    public R<?> register(@RequestBody RegisterBodyRequest registerBody)
    {
        // 用户注册
        sysLoginBiz.register(registerBody.getUsername(), registerBody.getPassword());
        return R.ok();
    }
}
