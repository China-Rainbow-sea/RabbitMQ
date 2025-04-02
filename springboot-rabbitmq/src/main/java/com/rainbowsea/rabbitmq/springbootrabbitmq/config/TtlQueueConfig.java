package com.rainbowsea.rabbitmq.springbootrabbitmq.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration  // 标注是一个配置类，加载到 IOC 容器当中
public class TtlQueueConfig {

    // 普通交换机的名称
    public static final String X_EXCHANGE = "X";
    // 死信队列的名称
    public static final String DEAD_LETTER_QUEUE = "QD";

    // 死信交换机的名称
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";


    // 普通队列的名称
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";


    //普通队列的名称
    public static final String QUEUE_C = "QC";





    // 声明队列 QC 同时绑定上 (以 RoutingKey 为 XC  )QD 死信交换机
    @Bean("queueC")
    public Queue queueC() {
        // 创建一个 Map 用于存放，设置队列信息
        Map<String, Object> arguments = new HashMap<>(3);
        // 设置死信交换机 注意这里的参数 x-dead-letter-exchange 是固定的不可
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        // 设置死信的 RoutingKey 为 YD ，注意这里的参数 x-dead-letter-routing-key 是固定的不可随便写
        arguments.put("x-dead-letter-routing-key", "YD");
        // 不设置 TTL 时间，也是通过生产者来设置
        return QueueBuilder.durable(QUEUE_C).withArguments(arguments).build();
    }

    // 将声明的队列 QC  绑定到 X 交换机当中
    @Bean  // 绑定也是要注入到 IOC 容器当中的
    public Binding queueBindingX(@Qualifier("queueC") Queue queueC,
                                 @Qualifier("xExchange") DirectExchange xExchange) {
        // 第一个参数是 队列名(注入到了IOC容器当中了)，第二个参数是交换机(注入到了IOC容器当中了)
        // 第三个蚕食是: 队列名对于绑定交换机之间的交互的  routingKey 信息"XC"
        // 表示将 队列 queueC 与 xExchange 交换机，通过 routingKey 为 "XC" 进行一个绑定
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }


    // 声明 XExchange 别名；会注入到 IOC 容器当中 普通交换机的
    @Bean(value = "xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }


    // 声明 yExchange 别名；会注入到 IOC 容器当中 普通交换机的
    @Bean(value = "yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    // 声明普通队列 TTL 为 10s
    @Bean("queueA")
    public Queue queueA() {
        // 创建一个 Map 用于存放，设置队列信息
        Map<String, Object> arguments = new HashMap<>(3);
        // 设置死信交换机 注意这里的参数 x-dead-letter-exchange 是固定的不可
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        // 设置死信的 RoutingKey 为 YD ，注意这里的参数 x-dead-letter-routing-key 是固定的不可随便写
        arguments.put("x-dead-letter-routing-key", "YD");
        // 设置 TTL ，单位是 ms 注意这里的参数 x-message-ttl 是固定的不可随便写
        arguments.put("x-message-ttl", 10000);

        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }


    // 声明普通队列 TTL 为 40s
    @Bean("queueB")
    public Queue queueB() {
        // 创建一个 Map 用于存放，设置队列信息
        Map<String, Object> arguments = new HashMap<>(3);
        // 设置死信交换机 注意这里的参数 x-dead-letter-exchange 是固定的不可
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        // 设置死信的 RoutingKey 为 YD ，注意这里的参数 x-dead-letter-routing-key 是固定的不可随便写
        arguments.put("x-dead-letter-routing-key", "YD");
        // 设置 TTL ，单位是 ms 注意这里的参数 x-message-ttl 是固定的不可随便写
        arguments.put("x-message-ttl", 40000);

        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }


    // 声明死信队列
    @Bean("queueD")
    public Queue queueD() {
        //  不带参数，就不需要 .withArguments(arguments)
        //return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }


    // 绑定

    /**
     * 注意：这里的  public Binding queueABindingX(@Qualifier("queueA") Queue queueA,
     *
     * @Qualifier("xExchange") DirectExchange xExchange)
     * @Qualifier("xExchange") DirectExchange xExchange)
     * @Qualifier("xExchange") DirectExchange xExchange)
     * @Qualifier 参数内的值是，我们上边：  @Bean(value = "xExchange")，@Bean("queueA") 注入到 IOC 容器当中的值
     * ，必须要是注入到了 IOC容器当中，不然是无法被使用上的，同时注意名称，不要写错了，要保持一致
     * @Bean(value = "xExchange")
     * public DirectExchange xExchange() {
     * @Bean("queueA") public Queue queueA() {
     */
    @Bean  // 绑定也是要注入到 IOC 容器当中的
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        // 第一个参数是 队列名(注入到了IOC容器当中了)，第二个参数是交换机(注入到了IOC容器当中了)
        // 第三个蚕食是: 队列名对于绑定交换机之间的交互的  routingKey 信息"XA"
        // 表示将 队列 queueA 与 xExchange 交换机，通过 routingKey 为 "XA" 进行一个绑定
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }


    // 绑定

    /**
     * 注意：这里的  public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,
     *
     * @Qualifier("xExchange") DirectExchange xExchange)
     * @Qualifier("xExchange") DirectExchange xExchange)
     * @Qualifier("xExchange") DirectExchange xExchange)
     * @Qualifier 参数内的值是，我们上边：  @Bean(value = "xExchange")，@Bean("queueB") 注入到 IOC 容器当中的值
     * ，必须要是注入到了 IOC容器当中，不然是无法被使用上的，同时注意名称，不要写错了，要保持一致
     * @Bean(value = "xExchange")
     * public DirectExchange xExchange() {
     * @Bean("queueB") public Queue queueB() {
     */
    @Bean  // 绑定也是要注入到 IOC 容器当中的
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        // 第一个参数是 队列名(注入到了IOC容器当中了)，第二个参数是交换机(注入到了IOC容器当中了)
        // 第三个蚕食是: 队列名对于绑定交换机之间的交互的  routingKey 信息"XB"
        // 表示将 队列 queueB 与 xExchange 交换机，通过 routingKey 为 "XB" 进行一个绑定
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }


    // 绑定

    /**
     * 注意：这里的  public Binding queueDBindingX(@Qualifier("queueD") Queue queueD,
     *
     * @Qualifier("yExchange") DirectExchange yExchange)
     * @Qualifier("xExchange") DirectExchange xExchange)
     * @Qualifier("xExchange") DirectExchange xExchange)
     * @Qualifier 参数内的值是，我们上边：  @Bean(value = "yExchange")，@Bean("queueD") 注入到 IOC 容器当中的值
     * ，必须要是注入到了 IOC容器当中，不然是无法被使用上的，同时注意名称，不要写错了，要保持一致
     * @Bean(value = "yExchange")
     * public DirectExchange yExchange() {
     * @Bean("queueD") public Queue queueD() {
     */
    @Bean // 绑定也是要注入到 IOC 容器当中的
    public Binding queueDBindingX(@Qualifier("queueD") Queue queueD,
                                  @Qualifier("yExchange") DirectExchange yExchange) {
        // 第一个参数是 队列名(注入到了IOC容器当中了)，第二个参数是交换机(注入到了IOC容器当中了)
        // 第三个蚕食是: 队列名对于绑定交换机之间的交互的  routingKey 信息"YD"
        // 表示将 队列 queueD 与 yExchange 交换机，通过 routingKey 为 "YD" 进行一个绑定
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }


}
