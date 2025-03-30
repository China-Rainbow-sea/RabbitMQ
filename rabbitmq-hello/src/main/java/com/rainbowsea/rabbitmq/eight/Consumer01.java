package com.rainbowsea.rabbitmq.eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rainbowsea.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;


/**
 * 死信队列-消费者1(正常队列)
 */
public class Consumer01 {

    // 普通交换机的名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    // 死信交换机的名称
    public static final String DEAD_EXCHANGE = "dead_exchange";

    // 普通队列的名称
    public static final String NORMAL_QUEUE = "normal_queue";
    // 死信队列的名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        // 声明死信和普通交换机类型为 direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        // 声明普通队列
        Map<String, Object> arguments = new HashMap<>();
        // 过期时间 10s = 10000ms 参数 key 是固定值的不可以随便写
        //arguments.put("x-message-ttl",100000);
        //正常队列设置死信交换机 参数 key 是固定值
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        // 设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", "lisi");
        // 设置正常队列的长度的限制,x-max-length参数 key 是固定值的,不可以随便写
        //arguments.put("x-max-length", 6);
        // 声明的是一个正常的队列
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);

        //////////////////////////////////////
        // 声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        // 绑定普通的交换机与普通的队列进行一个绑定(第一个参数是队列，第二个参数是交换机)
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        System.out.println("等待接收消息");

        // 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            // 获取到消息信息
            String msg = new String(message.getBody(), "UTF-8");
            if (msg.equals("info5")) {
                System.out.println("Consumer01 接收的消息是: " + msg + "此消息被 Consumer01 拒绝了");
                // 拒绝对应 message.getEnvelope().getDeliveryTag() 的信息
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("Consumer01 接收的消息: " + msg);
                // 手动应答，接收消息
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
        };

        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, consumerTag -> {
        });
    }
}
