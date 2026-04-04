package com.abtk.product.web.controller;

import com.abtk.product.service.redis.mq.UnifiedMessageProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rocketmq")
public class UnifiedRocketMQController {
    @Autowired
    private UnifiedMessageProducer messageProducer;

    // 普通消息 - 同步
    @PostMapping("/normal/sync")
    public String sendNormalSync(@RequestParam String message, @RequestParam String topic) {
        SendResult result = messageProducer.sendNormalSync(topic, message);
        return "同步发送普通消息成功: " + result.getSendStatus();
    }

    // 普通消息 - 异步
    @PostMapping("/normal/async")
    public String sendNormalAsync(@RequestParam String message, @RequestParam String topic) {
        messageProducer.sendNormalAsync(topic, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("异步发送成功: " + sendResult.getSendStatus());
            }

            @Override
            public void onException(Throwable e) {
                System.out.println("异步发送失败: " + e.getMessage());
            }
        });
        return "异步发送普通消息已提交";
    }

    // 普通消息 - 单向
    @PostMapping("/normal/oneway")
    public String sendNormalOneWay(@RequestParam String message, @RequestParam String topic) {
        messageProducer.sendNormalOneWay(topic, message);
        return "单向发送普通消息完成";
    }

    // 顺序消息 - 同步
    @PostMapping("/ordered/sync")
    public String sendOrderedSync(@RequestParam String message, @RequestParam String topic, @RequestParam String shardingKey) {
        SendResult result = messageProducer.sendOrderedSync(topic, message, shardingKey);
        return "同步发送顺序消息成功: " + result.getSendStatus();
    }

    // 顺序消息 - 异步
    @PostMapping("/ordered/async")
    public String sendOrderedAsync(@RequestParam String message, @RequestParam String topic, @RequestParam String shardingKey) {
        messageProducer.sendOrderedAsync(topic, message, shardingKey, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("顺序消息异步发送成功: " + sendResult.getSendStatus());
            }

            @Override
            public void onException(Throwable e) {
                System.out.println("顺序消息异步发送失败: " + e.getMessage());
            }
        });
        return "异步发送顺序消息已提交";
    }

    // 延时消息 - 同步
    @PostMapping("/delay/sync")
    public String sendDelaySync(@RequestParam String message, @RequestParam String topic, @RequestParam int delayLevel) {
        SendResult result = messageProducer.sendDelaySync(topic, message, delayLevel);
        return "同步发送延时消息成功: " + result.getSendStatus();
    }

    // 延时消息 - 异步
    @PostMapping("/delay/async")
    public String sendDelayAsync(@RequestParam String message, @RequestParam String topic, @RequestParam int delayLevel) {
        messageProducer.sendDelayAsync(topic, message, delayLevel, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("延时消息异步发送成功: " + sendResult.getSendStatus());
            }

            @Override
            public void onException(Throwable e) {
                System.out.println("延时消息异步发送失败: " + e.getMessage());
            }
        });
        return "异步发送延时消息已提交";
    }

    // 事务消息
    @PostMapping("/transaction")
    public String sendTransaction(@RequestParam String message, @RequestParam String topic, @RequestParam String bizData) {
        messageProducer.sendTransaction(topic, message, bizData);
        return "事务消息发送已提交";
    }
}
