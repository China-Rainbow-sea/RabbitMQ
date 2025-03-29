package com.rainbowsea.rabbitmq.two;


import com.rabbitmq.client.Channel;
import com.rainbowsea.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 生产者发送消息
 */
public class Task01 {

    // ctrl + shift + u 大写转换
    // 队列名称
    public static final String QUEUE_NAME = "hello";


    /**
     * 发送大量消息
     *
     * @param args
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();


        /*
        生成一个队列
        1.队列名称
        2. 队列里面的消息是否持久化(磁盘)，默认情况消息存储在内存中,false 表示不持久化
        3. 该队列是否只为一个消费者进行消费，是否进行消息共享，true 可以多个消费者消费；false 只能一个消费者消费
        4. 是否自动删除，最后一个消费者端打开连接以后，该队消息是否删除，true自动删除，false 不自动删除
        5.其它参数

         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 从控制台当中接受消息
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String message = scanner.next();
            /*
        发送一个消费
        1.发送到哪个交换机当中,本次空着，使用RabbitMQ默认交换机
        2.路由的Key值是哪个，本次是队列的名字
        3. 其它参数信息
        4. 发送消息的消息体,要转换为 bit 流进行处理发送
         */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("发送消息完成:" + message);
        }

    }
}
