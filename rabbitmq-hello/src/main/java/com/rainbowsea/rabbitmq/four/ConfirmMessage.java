package com.rainbowsea.rabbitmq.four;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rainbowsea.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * 发布确认模式：
 * 使用的时间：比较哪种确认方式是最好的
 * 1. 单个确认
 * 2. 批量确认
 * 3. 异步批量确认
 */
public class ConfirmMessage {

    // 批量发消息的个数
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws InterruptedException, TimeoutException, IOException {
        // 1.单个确认
        //ConfirmMessage.publishMessageIndividually(); // 发布1000个单独确认消息，耗时718ms
        //ConfirmMessage.publishMessageBatch(); // 发布1000批量100确认消息，耗时58ms
        ConfirmMessage.publishMessageAsync(); // 发布1000异步确认消息，耗时20ms

    }


    /**
     * 异步发布确认
     */
    public static void publishMessageAsync() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMQUtils.getChannel();

        // 开启发布确认
        channel.confirmSelect();

        //批量确认消息大小
        int batchSize = 100;

        // 队列的声明
        String queueName = UUID.randomUUID().toString();

        // 开启发布确认
        channel.waitForConfirms();

        // 消息确认成功，执行的回调函数
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            System.out.println("确认的消息" + deliveryTag);
        };

        // 消息确认失败，执行的回调函数
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            System.out.println("未确认的消息" + deliveryTag);
        };

        // 准备消息的监听器，监听哪些消息成功了，哪些消息失败了
        /*
         * 1. 监听哪些消息成功了
         * 2. 监听哪些消息失败了
         */
        channel.addConfirmListener(ackCallback, nackCallback);  // 异步通知

        // 开始时间
        long begin = System.currentTimeMillis();

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
        }

        // 结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "异步确认消息，耗时" + (end - begin) + "ms");

    }


    /**
     * 批量发布确认
     */
    public static void publishMessageBatch() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMQUtils.getChannel();

        // 开启发布确认
        channel.confirmSelect();

        //批量确认消息大小
        int batchSize = 100;

        String queueName = UUID.randomUUID().toString();

        // 开始时间
        long begin = System.currentTimeMillis();

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());

            if (i % batchSize == 0) {  // 到了 100 个消息，就发布确认，告诉交换机，我们该100个消息，都确认发布了
                channel.waitForConfirms();
            }
        }

        // 结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "批量" + batchSize + "确认消息，耗时" + (end - begin) + "ms");

    }

    /**
     * 单个确认
     */
    public static void publishMessageIndividually() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMQUtils.getChannel();

        // 开启发布确认
        channel.confirmSelect();

        String queueName = UUID.randomUUID().toString();

        // 开始时间
        long begin = System.currentTimeMillis();

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            // 单个消息就马上进行发布确认
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送成功");
            }
        }

        // 结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息，耗时" + (end - begin) + "ms");

    }
}
