package com.rainbowsea.rabbitmq.springbootrabbitmq.consumer;


import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * 消费者(消费/读取消息)
 */

@Slf4j
@Component  // 当 IOC 容器读取到
public class DeadLetterQueueConsumer {

    @RabbitListener(queues = "QD")
    public void receiveD(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("当前时间:{},基于插件收到死信队列的消息:{}", new Date().toString(), msg);
    }


}


