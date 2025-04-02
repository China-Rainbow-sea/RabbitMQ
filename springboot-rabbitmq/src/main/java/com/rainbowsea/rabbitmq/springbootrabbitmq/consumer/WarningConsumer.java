package com.rainbowsea.rabbitmq.springbootrabbitmq.consumer;


import com.rainbowsea.rabbitmq.springbootrabbitmq.config.ConfirmConfig2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WarningConsumer {


    @RabbitListener(queues = ConfirmConfig2.WARNING_QUEUE_NAME)  // 监听该队列的消息
    public void receiveWarningMsg(Message message) {
        String msg = new String(message.getBody());
        log.error("报警发现不可路由消息: {}", msg);

    }
}
