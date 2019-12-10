package com.dgh.main.admin.config.mq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

/** 
* @author 作者 dgh
* @date 创建时间：2019年11月13日 下午3:24:08 
* 类说明 
*/
@Component("userOrderListener")
public class UserOrderListener implements ChannelAwareMessageListener{

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		long tag = message.getMessageProperties().getDeliveryTag();
		try {
			byte[] body = message.getBody();
			String mobile = new String(body,"UTF-8");
			System.out.println("手机号：" + mobile);
//			channel.basicAck(tag, true);
			//multiple是否批量.true:将一次性拒绝所有小于deliveryTag的消息，requeue表示拒绝后是否重入队列
			channel.basicNack(tag, true, false);
		} catch (Exception e) {
			//异常后重入队列，处理重试次数
//			channel.basicNack(tag, false, true);
		}
		
	}
	
}
