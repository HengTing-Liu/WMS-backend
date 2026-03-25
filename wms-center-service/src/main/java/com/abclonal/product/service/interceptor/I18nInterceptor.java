package com.abclonal.product.service.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 国际化拦截器，从请求头中读取语言设置
 *
 * @author lht
 * @since 2026-03-09
 */
@Component
public class I18nInterceptor implements HandlerInterceptor {

    /**
     * 请求头中的语言字段名
     */
    private static final String LANG_HEADER = "Accept-Language";
    private static final String LANG_PARAM = "lang";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 1. 先从请求参数中获取语言
        String lang = request.getParameter(LANG_PARAM);
        
        // 2. 如果参数没有，从请求头获取
        if (lang == null || lang.isEmpty()) {
            lang = request.getHeader(LANG_HEADER);
        }
        
        // 3. 设置语言环境
        if (lang != null && !lang.isEmpty()) {
            LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
            if (localeResolver != null) {
                Locale locale = parseLocale(lang);
                localeResolver.setLocale(request, response, locale);
            }
        }
        
        return true;
    }

    /**
     * 解析语言标识为 Locale
     * 支持格式：zh-CN, zh_CN, en-US, en, ja-JP, ja 等
     */
    private Locale parseLocale(String lang) {
        if (lang == null || lang.isEmpty()) {
            return Locale.CHINESE;
        }
        
        // 处理 Accept-Language 头，可能包含权重信息，如 "zh-CN,zh;q=0.9,en;q=0.8"
        String mainLang = lang.split(",")[0].split(";")[0].trim();
        
        // 处理下划线格式（zh_CN）和连字符格式（zh-CN）
        String normalized = mainLang.replace("_", "-");
        String[] parts = normalized.split("-");
        
        if (parts.length >= 2) {
            return new Locale(parts[0], parts[1]);
        } else if (parts.length == 1) {
            return new Locale(parts[0]);
        }
        
        return Locale.CHINESE;
    }
}
