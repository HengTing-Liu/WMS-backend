package com.abclonal.product.jobs;

import com.abclonal.product.service.jobs.TestJobService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class TestJobHandler {
    @Resource
    private TestJobService testJobService;

    @XxlJob("testJobHandler")
    public void testJobHandler() throws Exception {
        // 获取参数
        String param = XxlJobHelper.getJobParam();

        // 业务逻辑
        log.info("XXL-JOB, TestJobHandler Hello World! Param: {}", param);

        //业务逻辑处理
        testJobService.testJob();

        // 设置分片参数（可选）
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();
        log.info("XXL-JOB, TestJobHandler 分片: {}/{}", shardIndex, shardTotal);
        // 成功返回
        XxlJobHelper.handleSuccess("任务执行成功");
    }
}
