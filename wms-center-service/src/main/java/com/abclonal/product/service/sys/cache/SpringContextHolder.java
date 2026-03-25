package com.abclonal.product.service.sys.cache;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Spring 上下文持有器
 * <p>
 * 用于在非 Spring 管理的对象中获取 Bean（如事件监听器、静态工具类等）。
 * 注意：仅在应用完全启动后才可使用。
 */
@Component
public class SpringContextHolder implements org.springframework.context.ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        applicationContext = ctx;
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    public static ApplicationContext getContext() {
        return applicationContext;
    }
}
