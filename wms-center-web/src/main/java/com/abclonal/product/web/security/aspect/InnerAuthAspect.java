package com.abclonal.product.web.security.aspect;


import com.abclonal.product.common.constant.SecurityConstants;
import com.abclonal.product.common.exception.InnerAuthException;
import com.abclonal.product.common.utils.ServletUtils;
import com.abclonal.product.common.utils.StringUtils;
import com.abclonal.product.service.system.service.I18nService;
import com.abclonal.product.web.security.annotation.InnerAuth;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 内部服务调用验证处理
 * 
 * @author ruoyi
 */
@Aspect
@Component
public class InnerAuthAspect implements Ordered
{
    @Autowired
    private I18nService i18nService;

    @Around("@annotation(innerAuth)")
    public Object innerAround(ProceedingJoinPoint point, InnerAuth innerAuth) throws Throwable
    {
        String source = ServletUtils.getRequest().getHeader(SecurityConstants.FROM_SOURCE);
        // 内部请求验证
        if (!StringUtils.equals(SecurityConstants.INNER, source))
        {
            throw new InnerAuthException(i18nService.getMessage("auth.inner.access.denied"));
        }

        String userid = ServletUtils.getRequest().getHeader(SecurityConstants.DETAILS_USER_ID);
        String username = ServletUtils.getRequest().getHeader(SecurityConstants.DETAILS_USERNAME);
        // 用户信息验证
        if (innerAuth.isUser() && (StringUtils.isEmpty(userid) || StringUtils.isEmpty(username)))
        {
            throw new InnerAuthException(i18nService.getMessage("auth.inner.user.required"));
        }
        return point.proceed();
    }

    /**
     * 确保在权限认证aop执行前执行
     */
    @Override
    public int getOrder()
    {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
