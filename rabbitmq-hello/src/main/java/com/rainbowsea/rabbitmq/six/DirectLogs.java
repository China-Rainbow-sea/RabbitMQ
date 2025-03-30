package com.rainbowsea.rabbitmq.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rainbowsea.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class DirectLogs {

    // 交换机名为 : direct_logs
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        // 声明一个交换机 BuiltinExchangeType.DIRECT(直接)
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"info",null,message.getBytes("UTF-8"));
            channel.basicPublish(EXCHANGE_NAME,"error",null,message.getBytes("UTF-8"));
            channel.basicPublish(EXCHANGE_NAME,"warning",null,message.getBytes("UTF-8"));
            System.out.println("生产者发出消息" + message);
        }
    }
}
