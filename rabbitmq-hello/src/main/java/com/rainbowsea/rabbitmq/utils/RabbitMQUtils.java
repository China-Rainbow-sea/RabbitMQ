package com.rainbowsea.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * 此类为连接工厂创建信道的工具类
 */
public class RabbitMQUtils {


    /**
     * 得到一个连接的 channel
     * @return
     * @throws IOException
     * @throws TimeoutException
     */
    public static Channel getChannel() throws IOException, TimeoutException {
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

        return channel;
    }
}
