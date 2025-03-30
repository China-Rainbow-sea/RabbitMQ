package com.rainbowsea.rabbitmq.five;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rainbowsea.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogs02 {

    // 交换机名为 : logs
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        // 声明一个交换机 fanout(扇出，广播式的)
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        // 声明一个队列，临时队列
        /*
        生产一个临时队列，队列的名称是随机的
        当消费者断开与该队列的连接的时候，队列就自动删除
         */
        String queueName = channel.queueDeclare().getQueue();

        // 绑定交换机与队列
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("等待接收消息，把接收到消息打印在屏幕上...");

        // 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogs02接收到的消息" + new String(message.getBody(), "UTF-8"));


        };

        // 消费者取消消息时回调接口
        channel.basicConsume(queueName, true, deliverCallback, consumerTage -> {
        });
    }
}
