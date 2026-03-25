package com.abclonal.product.service.redis.mq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Service
// 普通消息消费者
@RocketMQMessageListener(topic = "NormalTopic", consumerGroup = "normal-consumer-group")
@ConditionalOnProperty(prefix = "rocketmq", name = "enabled", havingValue = "true")
public class UnifiedNormalConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {

        System.out.println("【普通消息】接收消息: " + message);
    }
}
