package com.abclonal.product.web.security.config;

import com.abclonal.product.web.interceptor.AuthInterceptor;
import com.abclonal.product.web.interceptor.HeaderInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 *
 * @author ruoyi
 */
@Configuration("securityWebMvcConfig")
public class WebMvcConfig implements WebMvcConfigurer
{
    /** 不需要拦截地址 */
    public static final String[] excludeUrls = { "/login", "/logout", "/refresh" ,"/QrCodeProduct"};

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        // 1. 先注册 AuthInterceptor（鉴权）
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludeUrls)
                .order(-20); // 显式指定 order

        registry.addInterceptor(getHeaderInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(excludeUrls)
                .order(-10);
    }

    /**
     * 自定义请求头拦截器
     */
    public HeaderInterceptor getHeaderInterceptor()
    {
        return new HeaderInterceptor();
    }
}
