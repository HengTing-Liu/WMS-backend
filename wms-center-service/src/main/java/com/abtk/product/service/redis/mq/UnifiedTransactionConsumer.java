package com.abtk.product.service.redis.mq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Service
// 事务消息消费者
@RocketMQMessageListener(topic = "TxTopic", consumerGroup = "tx-consumer-group")
@ConditionalOnProperty(prefix = "rocketmq", name = "enabled", havingValue = "true")
public class UnifiedTransactionConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        System.out.println("【事务消息】接收消息: " + message);
    }
}
