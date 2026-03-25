package com.abclonal.product.service.redis.mq;

import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@RocketMQTransactionListener
public class UnifiedTransactionListener implements RocketMQLocalTransactionListener {
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object arg) {
        try {
            System.out.println("执行本地事务，业务数据: " + arg);
            // 执行本地业务逻辑（如数据库操作）
            // 这里可以访问消息内容: msg.getPayload()
            // 根据业务执行结果返回状态
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            System.out.println("本地事务执行失败: " + e.getMessage());
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        System.out.println("事务状态回查");
        // 这里应该查询数据库中的业务状态
        // 返回对应的事务状态
        return RocketMQLocalTransactionState.COMMIT;
    }
}
