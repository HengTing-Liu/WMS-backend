package com.abclonal.product.service.redis.mq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
// 普通消息消费者
@RocketMQMessageListener(topic = "QRCodeProductTopic", consumerGroup = "normal-consumer-group")
@ConditionalOnProperty(prefix = "rocketmq", name = "enabled", havingValue = "true")
public class QRCodeProductNormalConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {

        System.out.println("【普通消息】接收消息: " + message);
    }
}
