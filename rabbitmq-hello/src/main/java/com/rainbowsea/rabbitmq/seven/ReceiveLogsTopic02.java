package com.rainbowsea.rabbitmq.seven;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rainbowsea.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogsTopic02 {

    // 交换机名为 : topic_logs
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        // 声明一个交换机 BuiltinExchangeType.TOPIC(主题)
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        // 声明一个队列,队列名为 Q2
        String queueName = "Q2";
        channel.queueDeclare(queueName, false, false, false, null);
        // 绑定交换机与队列, 该 Q2 队列的 Routing key 为 *.orange.* 和 "lazy.#"
        channel.queueBind(queueName, EXCHANGE_NAME, "*.*.rabbit");
        channel.queueBind(queueName, EXCHANGE_NAME, "lazy.#");

        System.out.println("Q2 等待接收消息，把接收到消息打印在屏幕上...");

        // 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Q2 接收到的消息" + new String(message.getBody(), "UTF-8"));
            System.out.println("Q2 接收队列:" + queueName + "绑定键" + message.getEnvelope().getRoutingKey());


        };

        // 消费者取消消息时回调接口
        // 第一个参数，队列 queueName
        channel.basicConsume(queueName, true, deliverCallback, consumerTage -> {
        });
    }
}
