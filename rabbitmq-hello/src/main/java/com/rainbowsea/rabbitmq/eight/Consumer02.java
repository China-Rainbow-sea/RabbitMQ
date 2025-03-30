package com.rainbowsea.rabbitmq.eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rainbowsea.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;


/**
 * 死信队列-消费者1(死信队列)
 */
public class Consumer02 {
    ;
    // 死信交换机的名称
    public static final String DEAD_EXCHANGE = "dead_exchange";
    // 死信队列的名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        // 声明死信和普通交换机类型为 direct
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        // 绑定死信的交换机与死信的队列进行一个绑定(第一个参数是队列，第二个参数是交换机)
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");
        // 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer02 死信接收到的消息" + new String(message.getBody(), "UTF-8"));

        };

        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, consumerTag -> {
        });
    }
}
