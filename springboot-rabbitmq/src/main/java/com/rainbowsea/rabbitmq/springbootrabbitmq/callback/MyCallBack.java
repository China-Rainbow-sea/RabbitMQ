package com.rainbowsea.rabbitmq.springbootrabbitmq.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {


    @Resource  // 注入:
    private RabbitTemplate rabbitTemplate;


    @PostConstruct  // 表示一开始就会执行该函数当中的方法 进行一个初始化
    public void init() {
        // 注入: 因为我们的这个 ConfirmCallback 是  RabbitTemplate 的内部接口，
        // 我们无法直接对 RabbitTemplate 的内部接口进行一个直接的 注入。只能
        // 通过先对 RabbitTemplate 进行一个 注入，在将 ConfirmCallback set 进入到已经注入的 RabbitTemplate 类当中
        rabbitTemplate.setConfirmCallback(this);
        /**
         * true：
         *      交换机无法将消息进行路由时，会将该消息返回给生产者
         * false：
         *      如果发现消息无法进行路由，则直接丢弃
         */
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 可以在当消息传递过程中不可达目的地时，将消息返回给生产者
     * 只有不可达目的的时候，才进行回退，执行该函数
     *
     * @param returned
     */
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.info("消息{},被交换机 {} 回退，回退的原因:{},路由Key:{}", new String(returned.getMessage().getBody()),
                returned.getExchange(), returned.getReplyText(), returned.getRoutingKey()
        );
    }


    /**
     * 交换机确认回调方法
     * 1.发消息，交换机收到了，回调该方法
     * 第一个参数 CorrelationData 保存回调消息的ID，以及相关信息
     * 第二个参数: 交换机是否成功接受到了消息，true 表示接受到了，false 表示没有接受到
     * 第三个参数: 表示没有接受到消息的原因，成功了就为 null
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经收到 ID 为: {} 的消息", id);
        } else {
            log.info("交换机没有收到 ID 为: {} 的消息，由于原因:{}", id, cause);
        }
    }


}
