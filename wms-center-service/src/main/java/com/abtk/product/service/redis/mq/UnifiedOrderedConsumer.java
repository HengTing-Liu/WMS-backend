package com.abtk.product.service.redis.mq;

import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Service
// 顺序消息消费者
@RocketMQMessageListener(
        topic = "OrderTopic",
        consumerGroup = "order-consumer-group",
        consumeMode = ConsumeMode.ORDERLY // 顺序消费
)
@ConditionalOnProperty(prefix = "rocketmq", name = "enabled", havingValue = "true")
public class UnifiedOrderedConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println("【顺序消息】接收消息: " + message + ", 时间: " + System.currentTimeMillis());
        try {
            // 模拟业务处理时间，确保顺序性
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
