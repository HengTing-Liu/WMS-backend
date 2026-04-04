package com.abtk.product.service.system;


import com.abtk.product.common.domain.R;
import com.abtk.product.dao.entity.SysLogininfor;
import com.abtk.product.dao.entity.SysOperLog;
import com.abtk.product.service.system.service.I18nService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * 日志服务降级处理
 *
 * @author ruoyi
 */
//@Component
public class RemoteLogFallbackFactory implements FallbackFactory<RemoteLogService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteLogFallbackFactory.class);

    @Autowired
    private I18nService i18nService;

    @Override
    public RemoteLogService create(Throwable throwable)
    {
        log.error("日志服务调用失败:{}", throwable.getMessage());
        return new RemoteLogService()
        {
            @Override
            public R<Boolean> saveLog(SysOperLog sysOperLog, String source)
            {
                return R.fail(i18nService.getMessage("log.save.oper.failed", throwable.getMessage()));
            }

            @Override
            public R<Boolean> saveLogininfor(SysLogininfor sysLogininfor, String source)
            {
                return R.fail(i18nService.getMessage("log.save.login.failed", throwable.getMessage()));
            }
        };

    }
}
