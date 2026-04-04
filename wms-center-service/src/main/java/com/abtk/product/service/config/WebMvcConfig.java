package com.abtk.product.service.config;

import com.abtk.product.service.interceptor.I18nInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置类
 *
 * @author lht
 * @since 2026-03-09
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private I18nInterceptor i18nInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册国际化拦截器，拦截所有请求
        registry.addInterceptor(i18nInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**", "/favicon.ico");
    }
}
