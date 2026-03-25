package com.abclonal.product.service.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * 国际化服务类
 *
 * @author lht
 * @since 2026-03-09
 */
@Service
public class I18nService {

    @Autowired
    private MessageSource messageSource;

    /**
     * 获取当前语言环境下的消息
     *
     * @param key 消息 key
     * @return 消息内容
     */
    public String getMessage(String key) {
        return messageSource.getMessage(key, null, getCurrentLocale());
    }

    /**
     * 获取当前语言环境下的消息（带参数）
     *
     * @param key 消息 key
     * @param args 参数
     * @return 消息内容
     */
    public String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, getCurrentLocale());
    }

    /**
     * 获取指定语言环境下的消息
     *
     * @param key 消息 key
     * @param locale 语言环境
     * @return 消息内容
     */
    public String getMessage(String key, Locale locale) {
        return messageSource.getMessage(key, null, locale);
    }

    /**
     * 获取指定语言环境下的消息（带参数）
     *
     * @param key 消息 key
     * @param locale 语言环境
     * @param args 参数
     * @return 消息内容
     */
    public String getMessage(String key, Locale locale, Object... args) {
        return messageSource.getMessage(key, args, locale);
    }

    /**
     * 获取当前语言环境
     */
    public Locale getCurrentLocale() {
        return LocaleContextHolder.getLocale();
    }

    /**
     * 设置当前语言环境
     */
    public void setLocale(Locale locale) {
        LocaleContextHolder.setLocale(locale);
    }

    /**
     * 根据字符串设置语言环境
     *
     * @param lang 语言标识，如 zh-CN, en-US, ja-JP
     */
    public void setLocale(String lang) {
        Locale locale = parseLocale(lang);
        LocaleContextHolder.setLocale(locale);
    }

    /**
     * 解析语言标识为 Locale
     */
    private Locale parseLocale(String lang) {
        if (lang == null || lang.isEmpty()) {
            return Locale.CHINESE;
        }
        String[] parts = lang.split("-");
        if (parts.length == 2) {
            return new Locale(parts[0], parts[1]);
        } else if (parts.length == 1) {
            return new Locale(parts[0]);
        }
        return Locale.CHINESE;
    }
}
