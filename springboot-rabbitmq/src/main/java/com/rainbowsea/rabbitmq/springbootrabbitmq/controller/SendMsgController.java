package com.rainbowsea.rabbitmq.springbootrabbitmq.controller;


import com.rainbowsea.rabbitmq.springbootrabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;
import java.util.Date;

/**
 * 生产者：发送延时消息
 * http://localhost:8080/ttl/sendMsg/嘻嘻嘻
 */


@RestController
@RequestMapping("ttl")
@Slf4j
public class SendMsgController {

    @Resource  // 注入到 IOC 容器当中
    private RabbitTemplate rabbitTemplate;


    @GetMapping("sendDelayMsg/{message}/{delayTime}")
    public void sendMsg(@PathVariable String message, @PathVariable Integer delayTime) {
        log.info(" 当前时间: {}, 发送一条延迟{} 毫秒的信息给队列 delayed.queue:{}", new Date(), delayTime, message);
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME, DelayedQueueConfig.DELAYED_ROUTING_KEY, message,
                correlationData -> {
                    // 发送消息的时候，延迟时长，单位:ms
                    correlationData.getMessageProperties().setDelay(delayTime);
                    return correlationData;
                });
    }






    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable String message,
                        @PathVariable String ttlTime) {
        log.info("当前时间:{},发送一条时长{}毫秒 TTL 消息给队列QC:{}", new Date().toString(), ttlTime, message);
        rabbitTemplate.convertAndSend("X", "XC", message, msg -> {
            // 发送消息的时候，延时时长
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });

    }


    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        log.info("当前时间:{},发送一条消息给两个 TTL 队列:{}", new Date().toString(), message);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自ttl为 10s的队列" + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自ttl为 40s的队列" + message);

    }

}

