package com.rainbowsea.rabbitmq.eight;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rainbowsea.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class DeliverCallback {


    // 普通交换机的名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    // 死信交换机的名称
    public static final String DEAD_EXCHANGE = "dead_exchange";
    // 普通队列的名称
    public static final String NORMAL_QUEUE = "normal_queue";
    // 死信队列的名称
    public static final String DEAD_QUEUE = "dead_queue";


    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        // 声明死信和普通交换机类型为 direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        // 声明死信和普通队列
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, null);
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        // 接收消息
        com.rabbitmq.client.DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogsDirect01 接收到的消息" + new String(message.getBody(), "UTF-8"));

        };

        // 消费者取消消息时回调接口
        // 第一个参数，队列
        //channel.basicConsume(, true, deliverCallback, consumerTage -> {
        //});

    }

}
