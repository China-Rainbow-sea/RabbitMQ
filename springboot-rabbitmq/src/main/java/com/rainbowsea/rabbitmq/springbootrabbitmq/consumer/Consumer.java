package com.rainbowsea.rabbitmq.springbootrabbitmq.consumer;


import com.rainbowsea.rabbitmq.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;


/**
 * 结合 Spring Boot 实现“发布确认”  消费者——读取/消费消息
 */
@Component
@Slf4j
public class Consumer {


    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)  // 监听哪个队列
    public void receiveConfirmMessage(Message message) {
        String msg = new String(message.getBody());

        log.info("接受到的队列的 confirm.queue 队列的消息: {}", msg);

    }
}
