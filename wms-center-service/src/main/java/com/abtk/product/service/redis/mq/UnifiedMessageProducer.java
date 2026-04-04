package com.abtk.product.service.redis.mq;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class UnifiedMessageProducer {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    // 普通消息 - 同步发送
    public SendResult sendNormalSync(String topic, Object message) {
        SendResult result = rocketMQTemplate.syncSend(topic, message);
        System.out.println("同步发送普通消息成功: " + message);
        return result;
    }

    // 普通消息 - 异步发送
    public void sendNormalAsync(String topic, Object message, SendCallback callback) {
        rocketMQTemplate.asyncSend(topic, message, callback);
        System.out.println("异步发送普通消息: " + message);
    }

    // 普通消息 - 单向发送（无返回）
    public void sendNormalOneWay(String topic, Object message) {
        rocketMQTemplate.sendOneWay(topic, message);
        System.out.println("单向发送普通消息: " + message);
    }

    // 顺序消息 - 同步发送
    public SendResult sendOrderedSync(String topic, Object message, String shardingKey) {
        SendResult result = rocketMQTemplate.syncSendOrderly(topic, message, shardingKey);
        System.out.println("同步发送顺序消息成功: " + message + ", 分片键: " + shardingKey);
        return result;
    }

    // 顺序消息 - 异步发送
    public void sendOrderedAsync(String topic, Object message, String shardingKey, SendCallback callback) {
        rocketMQTemplate.asyncSendOrderly(topic, message, shardingKey, callback);
        System.out.println("异步发送顺序消息: " + message + ", 分片键: " + shardingKey);
    }

    // 延时消息 - 同步发送
    public SendResult sendDelaySync(String topic, Object message, int delayLevel) {
        org.springframework.messaging.Message<?> msg = MessageBuilder.withPayload(message).build();
        SendResult result = rocketMQTemplate.syncSend(topic, msg, 3000, delayLevel);
        System.out.println("同步发送延时消息成功: " + message + ", 延时等级: " + delayLevel);
        return result;
    }

    // 延时消息 - 异步发送
    public void sendDelayAsync(String topic, Object message, int delayLevel, SendCallback callback) {
        org.springframework.messaging.Message<?> msg = MessageBuilder.withPayload(message).build();
        rocketMQTemplate.asyncSend(topic, msg, callback, 3000, delayLevel);
        System.out.println("异步发送延时消息: " + message + ", 延时等级: " + delayLevel);
    }

    // 事务消息
    public void sendTransaction(String topic, Object message, Object bizData) {
        org.springframework.messaging.Message<?> msg = MessageBuilder.withPayload(message).build();
        rocketMQTemplate.sendMessageInTransaction(topic, msg, bizData);
        System.out.println("发送事务消息: " + message + ", 业务数据: " + bizData);
    }
}
