package com.abtk.product.service.system;


import com.abtk.product.dao.entity.SysOperLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步调用日志服务
 *
 * @author ruoyi
 */
@Service
public class AsyncLogService {
    //    @Autowired
//    private RemoteLogService remoteLogService;
    @Autowired
    private ISysOperLogService sysOperLogService;

    /**
     * 保存系统日志记录
     */
    @Async
    public void saveSysLog(SysOperLog sysOperLog) throws Exception {
        sysOperLogService.insertOperlog(sysOperLog);
//        remoteLogService.saveLog(sysOperLog, SecurityConstants.INNER);
    }
}
