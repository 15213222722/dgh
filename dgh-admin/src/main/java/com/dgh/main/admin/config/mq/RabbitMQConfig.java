package com.dgh.main.admin.config.mq;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


/** 
* @author 作者 dgh
* @date 创建时间：2019年11月11日 下午3:09:37 
* 类说明 
*/
@Configuration
	public class RabbitMQConfig {
	
	@Autowired
    private Environment env;
 
    @Autowired
    private CachingConnectionFactory connectionFactory;
 
//    @Autowired
//    private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;
 
    /**
     * 单一消费者
     * @return
     */
//    @Bean(name = "singleListenerContainer")
//    public SimpleRabbitListenerContainerFactory listenerContainer(){
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setMessageConverter(new Jackson2JsonMessageConverter());
//        factory.setConcurrentConsumers(1);
//        factory.setMaxConcurrentConsumers(1);
//        factory.setPrefetchCount(1);
//        factory.setTxSize(1);
//        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
//        return factory;
//    }
 
    /**
     * 多个消费者
     * @return
     */
//    @Bean(name = "multiListenerContainer")
//    public SimpleRabbitListenerContainerFactory multiListenerContainer(){
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factoryConfigurer.configure(factory,connectionFactory);
//        factory.setMessageConverter(new Jackson2JsonMessageConverter());
//        factory.setAcknowledgeMode(AcknowledgeMode.NONE);
//        factory.setConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.concurrency",int.class));
//        factory.setMaxConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.max-concurrency",int.class));
//        factory.setPrefetchCount(env.getProperty("spring.rabbitmq.listener.prefetch",int.class));
//        return factory;
//    }
 
    @SuppressWarnings("deprecation")
	@Bean
    public RabbitTemplate rabbitTemplate(){
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
//        rabbitTemplate.setMessageConverter();
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            	 if (ack) {
                     System.out.println(String.format("消息成功发送到Exchange:correlationData(%s)",correlationData));
                 } else {
                	 System.out.println(String.format("消息发送到Exchange失败:cause(%s)",cause));
                 }
                
            }
        });
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
            	 System.out.println(String.format("消息从Exchange路由到Queue失败:exchange(%s),route(%s),replyCode(%s),replyText(%s),message:%s",exchange,routingKey,replyCode,replyText,message));
            }
        });
        return rabbitTemplate;
    }
    

//	@Bean(name="serializerMessageConverter")
//	public MessageConverter getMessageConverter(){
//	        return new SimpleMessageConverter();
//	}
    
	@Bean(name="messagePropertiesConverter")
	public MessagePropertiesConverter getMessagePropertiesConverter(){
	    return new DefaultMessagePropertiesConverter();
	}
    
    @Autowired
    private UserOrderListener userOrderListener;
    
    @Bean
    public SimpleMessageListenerContainer listenerContainerUserOrder(@Qualifier("singlePhoneMessageQueue") Queue singlePhoneMessageQueue) {
    	SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
    	simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
    	simpleMessageListenerContainer.setMessagePropertiesConverter(getMessagePropertiesConverter());
    	//并发配置
    	simpleMessageListenerContainer.setConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.concurrency",int.class));
    	simpleMessageListenerContainer.setMaxConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.max-concurrency",int.class));
    	simpleMessageListenerContainer.setPrefetchCount(env.getProperty("spring.rabbitmq.listener.prefetch",int.class));
    	//消息确认机制
    	simpleMessageListenerContainer.setQueues(singlePhoneMessageQueue);
    	simpleMessageListenerContainer.setMessageListener(userOrderListener);
    	simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
    	//全局的转换器:
        ContentTypeDelegatingMessageConverter convert = new ContentTypeDelegatingMessageConverter();
        
        MessageListenerAdapter adapter = new MessageListenerAdapter();
        TextMessageConverter textConvert = new TextMessageConverter();
        convert.addDelegate("text", textConvert);
        convert.addDelegate("html/text", textConvert);
        convert.addDelegate("xml/text", textConvert);
        convert.addDelegate("text/plain", textConvert);

        Jackson2JsonMessageConverter jsonConvert = new Jackson2JsonMessageConverter();
        convert.addDelegate("json", jsonConvert);
        convert.addDelegate("application/json", jsonConvert);

        ImageMessageConverter imageConverter = new ImageMessageConverter();
        convert.addDelegate("image/png", imageConverter);
        convert.addDelegate("image", imageConverter);

        PDFMessageConverter pdfConverter = new PDFMessageConverter();
        convert.addDelegate("application/pdf", pdfConverter);
        adapter.setMessageConverter(convert);
    	return simpleMessageListenerContainer;
    }
    
    
    
    
    
    
    
}
