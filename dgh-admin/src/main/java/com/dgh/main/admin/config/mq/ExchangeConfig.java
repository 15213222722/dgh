package com.dgh.main.admin.config.mq;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** 
* @author 作者 dgh
* @date 创建时间：2019年11月13日 下午4:23:47 
* 类说明 
*/
@Configuration
public class ExchangeConfig {

    // 单个短信发送exchage
    @Bean
    public DirectExchange singlePhoneMessageDirectExchage() {
        return new DirectExchange(QueueEnum.SINGLE_PHONE_MESSAGE_QUEUE.getExchange(), true, false);
    }

    // 单个短信发送exchage
    @Bean
    public DirectExchange singlePhoneMessageTtlDirectExchage() {
        return new DirectExchange(QueueEnum.SINGLE_PHONE_MESSAGE_TTL_QUEUE.getExchange(), true, false);
    }

    // 单个短信发送exchage
    @Bean
    public DirectExchange singlePhoneMessageDeadDirectExchage() {
        return new DirectExchange(QueueEnum.SINGLE_PHONE_MESSAGE_DEAD_QUEUE.getExchange(), true, false);
    }

    // @Bean
    // TopicExchange topicExchange() {
    //     return new TopicExchange("topicExchange", true, false);
    // }
    //
    // @Bean
    // FanoutExchange fanoutExchange() {
    //     return new FanoutExchange("fanoutExchange", true, false);
    // }

}
