package com.abtk.product.service.aspectj;

import com.abtk.product.common.datasource.DataSource;
import com.abtk.product.common.datasource.DataSourceContextHolder;
import com.abtk.product.common.datasource.DataSourceType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;

/**
 *
 *
 * @description:
 * @author: 75618
 * @time: 2026/2/24 16:08
 *
 */
@Aspect
@Component
public class DataSourceAspect {
    // 优先级高于 @Transactional（确保先设置数据源）
//    @Before("@within(com.abtk.product.common.datasource.DataSource) || @annotation(com.abtk.product.common.datasource.DataSource)")
    @Before("execution(* com.abtk.product.dao..*.*(..))")
    public void switchDataSource(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        // 事务中强制主库
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            DataSourceContextHolder.set(DataSourceType.MASTER);
            return;
        }

        // 先查方法，再查类
//        DataSource ds = method.getAnnotation(DataSource.class);
//        if (ds == null) {
//            ds = method.getDeclaringClass().getAnnotation(DataSource.class);
//        }
        DataSource ds = AnnotatedElementUtils.findMergedAnnotation(method, DataSource.class);
        if (ds == null) {
            ds = AnnotatedElementUtils.findMergedAnnotation(method.getDeclaringClass(), DataSource.class);
        }

        if (ds != null) {
            DataSourceContextHolder.set(ds.value());
        } else {
            // 默认：判断是否是查询方法（简单规则）
            String methodName = method.getName();
            if (methodName.startsWith("select") ||
                    methodName.startsWith("get") ||
                    methodName.startsWith("list") ||
                    methodName.startsWith("query") ||
                    methodName.startsWith("check") ||
                    methodName.startsWith("count") ||
                    methodName.startsWith("has")
            ) {
                DataSourceContextHolder.set(DataSourceType.SLAVE);
            } else {
                DataSourceContextHolder.set(DataSourceType.MASTER);
            }
        }
    }

    @After("execution(* com.abtk.product.dao..*.*(..))")
    public void clearDataSource() {
        DataSourceContextHolder.clear();
    }
}
