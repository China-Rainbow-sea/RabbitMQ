package com.rainbowsea.rabbitmq.springbootrabbitmq.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DelayedQueueConfig {

    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";


    @Bean("delayedQueue")  // 声明队列，基于 rabbitmq_delayed_message_exchange 插件的
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }

    // 声明交换机(可以算是一种自定义交换机)，基于 rabbitmq_delayed_message_exchange 插件的
    @Bean("delayedExchange")
    public CustomExchange delayedExchange() {
        // 创建一个 Map 用于存放，设置队列信息
        Map<String, Object> arguments = new HashMap<>(3);
        // 自定义交换机的类型, 这里定义为 direct 直接类型
        arguments.put("x-delayed-type", "direct");
        /**
         * CustomExchange 方法参数
         * 1.交换机的名称
         * 2.交换机的类型
         * 3.是否需要持久化
         * 4.是否需要自动删除
         * 5.其它的参数
         */

        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, arguments);
    }


    // 基于 rabbitmq_delayed_message_exchange 插件的 将队列绑定到交换机当中
    @Bean
    public Binding delayedQueueBindingdelayedExchange(
            @Qualifier("delayedQueue") Queue delayedQueue,
            @Qualifier("delayedExchange") CustomExchange delayedExchange) {
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}
