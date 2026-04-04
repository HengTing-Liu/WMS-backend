package com.abtk.product.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * 国际化配置类
 *
 * @author lht
 * @since 2026-03-09
 */
@Configuration
public class I18nConfig {

    /**
     * 配置 LocaleResolver，从请求头或参数中读取语言
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        // 默认中文
        resolver.setDefaultLocale(Locale.CHINESE);
        return resolver;
    }

    /**
     * 配置 MessageSource，加载 i18n 资源文件
     */
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("i18n/messages");
        source.setDefaultEncoding("UTF-8");
        // 找不到 key 时不抛出异常，返回 key 本身
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }
}
