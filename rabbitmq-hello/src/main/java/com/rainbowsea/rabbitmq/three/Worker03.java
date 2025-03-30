package com.rainbowsea.rabbitmq.three;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rainbowsea.rabbitmq.utils.RabbitMQUtils;
import com.rainbowsea.rabbitmq.utils.SleepUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Worker03 {

    // ctrl + shift + u 大写转换
    // 队列名称
    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("C1 等待接收消息处理时间较短");

        // 消费者未成功消费/读取到队列当中的信息后，的执行的回调函数
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "消费者取消消费接口执行回调逻辑");
        };

        // 声明: 消费者成功消费/读取到队列当中的信息后，的执行的回调函数
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            // new String(message.getBody(),"UTF-8" 如果这里接收的消息内容是中文的，需要将其转换为 utf-8的内容
            System.out.println("读取到的消息" + new String(message.getBody(),"UTF-8"));

            //  读取时，睡眠 1 s
            SleepUtils.sleep(1);

            /*
            手动应答
            1. 消息的标记 tag
            2. 是否批量应答 false : 不批量应答信道中的消息 true 批量，false 不批量
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };

        /*
        消费者消费/读取消息
        1.消费/读取哪个队列当中的消息(注意：一旦读取到了该队列中的某条消息，该消息就被消费者消费掉了，就从队列当中删除了)
        2.消费成功之后是否要自动应答 true 代表的自动应答，false 代表手动应答
        3.消费者未成功消费/读取到队列当中的信息后，的执行的回调函数
        4.消费者成功消费/读取到队列当中的信息后，的执行的回调函数
         */

        // 设置不公平分发
        //int prefetchCount = 1;

        // 设置预取值为: 2
        int prefetchCount = 2;
        channel.basicQos(prefetchCount);

        boolean autoAck = false;  // 采用手动应答的方式
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
    }
}
