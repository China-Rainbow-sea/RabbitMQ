package com.rainbowsea.rabbitmq.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rainbowsea.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogsDirect02 {

    // 交换机名为 : direct_logs
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        // 声明一个交换机  BuiltinExchangeType.DIRECT(直接)
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 声明一个队列,队列名为 disk
        channel.queueDeclare("console", false, false, false, null);
        // 绑定交换机与队列, 该 console 队列的 Routing key 为 info和 warning 两个
        channel.queueBind("console", EXCHANGE_NAME, "info");
        channel.queueBind("console", EXCHANGE_NAME, "warning");

        System.out.println("ReceiveLogsDirect02 等待接收消息，把接收到消息打印在屏幕上...");

        // 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogsDirect02 接收到的消息" + new String(message.getBody(), "UTF-8"));


        };

        // 消费者取消消息时回调接口
        // 第一个参数，队列 console
        channel.basicConsume("console", true, deliverCallback, consumerTage -> {
        });
    }
}
