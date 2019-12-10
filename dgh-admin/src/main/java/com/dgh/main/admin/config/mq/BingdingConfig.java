package com.dgh.main.admin.config.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** 
* @author 作者 dgh
* @date 创建时间：2019年11月13日 下午4:24:12 
* 类说明 
*/
@Configuration
public class BingdingConfig {

    // 单个短信发送绑定
    @Bean
    public Binding singlePhoneMessageDirectBingding(Queue singlePhoneMessageQueue, DirectExchange singlePhoneMessageDirectExchage){
        return BindingBuilder.bind(singlePhoneMessageQueue).to(singlePhoneMessageDirectExchage).with(QueueEnum.SINGLE_PHONE_MESSAGE_QUEUE.getRouteKey());
    }

    // 单个短信发送延时绑定
    @Bean
    public Binding singlePhoneMessageTtlDirectBingding(Queue singlePhoneMessageTtlQueue, DirectExchange singlePhoneMessageTtlDirectExchage){
        return BindingBuilder.bind(singlePhoneMessageTtlQueue).to(singlePhoneMessageTtlDirectExchage).with(QueueEnum.SINGLE_PHONE_MESSAGE_TTL_QUEUE.getRouteKey());
    }

    // 单个短信发送死信绑定
    @Bean
    public Binding singlePhoneMessageDeadDirectBingding(Queue singlePhoneMessageDeadQueue, DirectExchange singlePhoneMessageDeadDirectExchage){
        return BindingBuilder.bind(singlePhoneMessageDeadQueue).to(singlePhoneMessageDeadDirectExchage).with(QueueEnum.SINGLE_PHONE_MESSAGE_DEAD_QUEUE.getRouteKey());
    }
    // /**
    //  * 将队列topic.message与exchange绑定，binding_key为topic.message,就是完全匹配
    //  */
    // @Bean
    // Binding bindingExchangeMessage(Queue queueMessage, TopicExchange exchange) {
    //     return BindingBuilder.bind(queueMessage).to(exchange).with("topic.message");
    // }
    //
    // /**
    //  * 将队列topic.messages与exchange绑定，binding_key为topic.#,模糊匹配
    //  */
    // @Bean
    // Binding bindingExchangeMessages(Queue queueMessages, TopicExchange exchange) {
    //     return BindingBuilder.bind(queueMessages).to(exchange).with("topic.#");
    // }
    //
    // @Bean
    // Binding bindingExchangeA(Queue AMessage,FanoutExchange fanoutExchange) {
    //     return BindingBuilder.bind(AMessage).to(fanoutExchange);
    // }
    //
    // @Bean
    // Binding bindingExchangeB(Queue BMessage, FanoutExchange fanoutExchange) {
    //     return BindingBuilder.bind(BMessage).to(fanoutExchange);
    // }
    //
    // @Bean
    // Binding bindingExchangeC(Queue CMessage, FanoutExchange fanoutExchange) {
    //     return BindingBuilder.bind(CMessage).to(fanoutExchange);
    // }

}
