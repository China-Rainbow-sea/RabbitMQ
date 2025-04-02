package com.rainbowsea.rabbitmq.springbootrabbitmq.consumer;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.rainbowsea.rabbitmq.springbootrabbitmq.config.DelayedQueueConfig;

import java.util.Date;


/**
 * 消费者，消费/读取消息，基于rabbitmq_delayed_message_exchange 插件
 */
@Slf4j
@Component
public class DelayQueueConsumer {

    // 监听消息
    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
    public void receiveDelayQueue(Message message) {
        String msg = new String(message.getBody());
        log.info("当前时间:{} ,收到延时队列的消息:{}", new Date().toString(), msg);
    }
}
