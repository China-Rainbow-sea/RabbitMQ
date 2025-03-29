package com.rainbowsea.rabbitmq.one;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者，发送消息
 */
public class Producer {
    // ctrl + shift + u 大写转换
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
        /*
        生成一个队列
        1.队列名称
        2. 队列里面的消息是否持久化(磁盘)，默认情况消息存储在内存中,false 表示不持久化
        3. 该队列是否只为一个消费者进行消费，是否进行消息共享，true 可以多个消费者消费；false 只能一个消费者消费
        4. 是否自动删除，最后一个消费者端打开连接以后，该队消息是否删除，true自动删除，false 不自动删除
        5.其它参数

         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        // 发消息
        String message = "Hello World"; // 初次发送消息
        /*
        发送一个消费
        1.发送到哪个交换机当中,本次空着，使用RabbitMQ默认交换机
        2.路由的Key值是哪个，本次是队列的名字
        3. 其它参数信息
        4. 发送消息的消息体,要转换为 bit 流进行处理发送
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("消息发送完毕");

    }
}
