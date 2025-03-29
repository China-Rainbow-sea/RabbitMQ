package com.rainbowsea.rabbitmq.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rainbowsea.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Worker02 {

    // ctrl + shift + u 大写转换
    // 队列名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        // 声明: 消费者成功消费/读取到队列当中的信息后，的执行的回调函数
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("读取到的消息" + new String(message.getBody()));
        };

        // 消费者未成功消费/读取到队列当中的信息后，的执行的回调函数
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "消费者取消消费接口执行回调逻辑");
        };


        /*
        消费者消费/读取消息
        1.消费/读取哪个队列当中的消息(注意：一旦读取到了该队列中的某条消息，该消息就被消费者消费掉了，就从队列当中删除了)
        2.消费成功之后是否要自动应答 true 代表的自动应答，false 代表手动应答
        3.消费者未成功消费/读取到队列当中的信息后，的执行的回调函数
        4.消费者成功消费/读取到队列当中的信息后，的执行的回调函数
         */
        System.out.println("C2 等待接收消息");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
