package com.dgh.main.admin.config.mq;
import java.util.HashMap;
import java.util.Map;

/** 
* @author 作者 dgh
* @date 创建时间：2019年11月13日 下午4:22:30 
* 类说明 
*/
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Zhang ZhiJun
 * @Date: Created in 2018/08/28 16:20
 * @Description:
 */
@Configuration
public class QueueConfig {

    /**
     * 单个短信发送队列
     */
    @Bean
    public Queue singlePhoneMessageQueue() {
        Map<String,Object> args=new HashMap<>();
        // 设置该Queue的死信的信箱
        args.put("x-dead-letter-exchange", QueueEnum.SINGLE_PHONE_MESSAGE_DEAD_QUEUE.getExchange());
        // 设置死信routingKey
        args.put("x-dead-letter-routing-key", QueueEnum.SINGLE_PHONE_MESSAGE_DEAD_QUEUE.getRouteKey());
        return new Queue(QueueEnum.SINGLE_PHONE_MESSAGE_QUEUE.getName(), true, false, false, args);
    }

    /**
     * 单个短信发送ttl队列
     */
    @Bean
    public Queue singlePhoneMessageTtlQueue() {
        Map<String,Object> args=new HashMap<>();
        // 设置该Queue的死信的信箱
        args.put("x-dead-letter-exchange", QueueEnum.SINGLE_PHONE_MESSAGE_QUEUE.getExchange());
        // 设置死信routingKey
        args.put("x-dead-letter-routing-key", QueueEnum.SINGLE_PHONE_MESSAGE_QUEUE.getRouteKey());
        return new Queue(QueueEnum.SINGLE_PHONE_MESSAGE_TTL_QUEUE.getName(), true, false, false, args);
    }

    /**
     * 单个短信发送dead队列
     */
    @Bean
    public Queue singlePhoneMessageDeadQueue(){
        return new Queue(QueueEnum.SINGLE_PHONE_MESSAGE_DEAD_QUEUE.getName(), true);
    }

    // @Bean
    // public Queue userQueue() {
    //     return new Queue("user", true);
    // }
    //
    // //===============以下是验证topic Exchange的队列==========
    // @Bean
    // public Queue queueMessage() {
    //     return new Queue("topic.message", true);
    // }
    //
    // @Bean
    // public Queue queueMessages() {
    //     return new Queue("topic.messages", true);
    // }
    // //===============以上是验证topic Exchange的队列==========
    //
    //
    // //===============以下是验证Fanout Exchange的队列==========
    // @Bean
    // public Queue AMessage() {
    //     return new Queue("fanout.A", true);
    // }
    //
    // @Bean
    // public Queue BMessage() {
    //     return new Queue("fanout.B", true);
    // }
    //
    // @Bean
    // public Queue CMessage() {
    //     return new Queue("fanout.C", true);
    // }
    //===============以上是验证Fanout Exchange的队列==========

}
