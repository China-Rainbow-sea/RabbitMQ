package com.rainbowsea.rabbitmq.one;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;


import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * 消费者读取消息，
 * 消费消息
 */
public class Consumer {

    // 队列名称
    public static final String QUEUE_NAME = "hello";


    // 生产者，发送消息
    public static void main(String[] args) throws IOException, TimeoutException {

        // 创建一个连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 工厂IP连接 RabbitMQ的队列
        connectionFactory.setHost("192.168.76.156"); // Rabbitmq 所在的IP地址
        // 连接 RabbitMQ 的用户名
        connectionFactory.setUsername("admin");
        // 连接 RabbitMQ 的密码
        connectionFactory.setPassword("123");
        // 创建连接
        Connection connection = connectionFactory.newConnection();
        // 获取信道
        Channel channel = connection.createChannel();


        // 声明: 消费者成功消费/读取到队列当中的信息后，的执行的回调函数
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("读取到的消息" + message);
            String messages = new String(message.getBody());
            System.out.println("读取到的消息" + messages);
        };

        // 消费者未成功消费/读取到队列当中的信息后，的执行的回调函数
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("没有读取到的消息，消息在读取过程中中断了");
        };


        /*
        消费者消费/读取消息
        1.消费/读取哪个队列当中的消息(注意：一旦读取到了该队列中的某条消息，该消息就被消费者消费掉了，就从队列当中删除了)
        2.消费成功之后是否要自动应答 true 代表的自动应答，false 代表手动应答
        3.消费者未成功消费/读取到队列当中的信息后，的执行的回调函数
        4.消费者成功消费/读取到队列当中的信息后，的执行的回调函数
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);


    }
}
