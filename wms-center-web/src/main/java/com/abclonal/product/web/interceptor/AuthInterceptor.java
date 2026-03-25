package com.abclonal.product.web.interceptor;

import com.abclonal.product.common.constant.CacheConstants;
import com.abclonal.product.common.constant.SecurityConstants;
import com.abclonal.product.common.constant.TokenConstants;
import com.abclonal.product.common.utils.JwtUtils;
import com.abclonal.product.common.utils.StringUtils;
import com.abclonal.product.service.redis.service.RedisService;
import com.abclonal.product.service.system.service.I18nService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    @Autowired
    private I18nService i18nService;

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    // 白名单路径（可从配置文件读取）
    private static final String[] WHITE_URLS = {
            "/QrCodeProduct/createQrcode",
            "/api/test",
            "/api/login",
            "/api/register",
            "/api/captcha/image",
            "/system/meta/**",
            "/crud/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-ui",
            "/swagger-ui/",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/error"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 非 Controller 方法直接放行（如静态资源）
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String url = request.getRequestURI();
        log.info("url={}", url);
        // 跳过白名单
        for (String whiteUrl : WHITE_URLS) {
            if (PATH_MATCHER.match(whiteUrl, url)) {
                log.info("Match whitelist: {} -> {}", whiteUrl, url);
                return true;
            }
        }
        log.warn("Not match whitelist: {}", url);
        // 获取 Token
        String token = getToken(request);
        if (StringUtils.isEmpty(token)) {
            unauthorized(request, response, i18nService.getMessage("auth.token.empty"));
            return false;
        }

        Claims claims = JwtUtils.parseToken(token);
        if (claims == null) {
            unauthorized(request, response, i18nService.getMessage("auth.token.expired"));
            return false;
        }

        String userkey = JwtUtils.getUserKey(claims);
        String tokenKey = CacheConstants.LOGIN_TOKEN_KEY + userkey;
        if (!redisService.hasKey(tokenKey)) {
            unauthorized(request, response, i18nService.getMessage("auth.login.expired"));
            return false;
        }

        String userid = JwtUtils.getUserId(claims);
        String username = JwtUtils.getUserName(claims);
        if (StringUtils.isEmpty(userid) || StringUtils.isEmpty(username)) {
            unauthorized(request, response, i18nService.getMessage("auth.token.invalid"));
            return false;
        }

        // 将用户信息存入 request attribute（供 Controller 使用）
        request.setAttribute(SecurityConstants.DETAILS_USER_ID, userid);
        request.setAttribute(SecurityConstants.DETAILS_USERNAME, username);
        request.setAttribute(SecurityConstants.USER_KEY, userkey);

        return true;
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)) {
            token = token.substring(TokenConstants.PREFIX.length());
        }
        return token;
    }

    private void unauthorized(HttpServletRequest request, HttpServletResponse response, String msg) throws Exception {
        log.error("[Auth Exception] Request path: {}, Error: {}", request.getRequestURI(), msg);
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write("{\"code\":401,\"msg\":\"" + msg + "\"}");
        writer.flush();
        writer.close();
    }
}
