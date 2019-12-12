package org.dgh.admin;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dgh.main.admin.AdminApplication;
import com.dgh.main.admin.config.mq.QueueEnum;
import com.dgh.main.admin.dubbo.service.PayRpcService;
import com.dgh.main.admin.entity.OrderPayRecord;
import com.dgh.main.admin.redis.service.JedisService;
import com.dgh.main.admin.service.impl.OrderPayRecordServiceImpl;

/**
 * Unit test for simple App.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdminApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppTest {

	@Autowired
	private OrderPayRecordServiceImpl orderPayRecordService;
	@Autowired
    RabbitTemplate rabbitTemplate;
	
	@Test
	public void contextLoads() {
	}

	/**
	 * 测试service
	 * @throws Exception 
	 */
	@Test
	@Transactional
	public void testService() throws Exception {
		OrderPayRecord orderPayRecord = new OrderPayRecord();
		orderPayRecord.setCreateTime(LocalDateTime.now());
		orderPayRecord.setOrderDesc("订单描述");
		orderPayRecord.setOrderNo(UUID.randomUUID() + "");
		orderPayRecord.setOrderTitle("我是订单");
		orderPayRecord.setPayConfigId(1L);
		orderPayRecord.setPayMethod("alipay");
		orderPayRecord.setPayStatus(0);
		orderPayRecord.setPayStatusDesc("未支付");
		orderPayRecord.setPayType("PC");
		orderPayRecord.setPayWay("alipay-wap");
		orderPayRecord.setProductCode(UUID.randomUUID() + "");
		orderPayRecord.setTotalAmount(123);
		orderPayRecord.setUpdateTime(LocalDateTime.now());
		orderPayRecord.setVersion(0);
		boolean flag = orderPayRecordService.save(orderPayRecord);
		//乐观锁测试
		OrderPayRecord order = new OrderPayRecord();
//		order.setId(orderPayRecord.getId());
		order.setOrderNo(UUID.randomUUID() + "aa");
		order.setVersion(orderPayRecord.getVersion());
		UpdateWrapper<OrderPayRecord> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id", orderPayRecord.getId());
		orderPayRecordService.update(order, updateWrapper);
		
//		System.out.println("------" + orderPayRecord.getId());
//		orderPayRecord.setOrderTitle("我是订单22");
//		UpdateWrapper<OrderPayRecord> wrapper = new UpdateWrapper<OrderPayRecord>();
//		wrapper.eq("order_no", "6589898998");
//		wrapper.and(new Consumer<UpdateWrapper<OrderPayRecord>>() {
//			@Override
//			public void accept(UpdateWrapper<OrderPayRecord> t) {
//				t.eq("pay_status", 0);
//			}
//		});
//		boolean flag = orderPayRecordService.update(orderPayRecord,wrapper);
		System.out.println(flag);
	}
	
	/**
	 *	 测试分页
	 * @return 
	 */
//	@Test
	public void testPage() {
		Page<OrderPayRecord> page = new Page<>(1, 10);
		page.addOrder(OrderItem.desc("id"));
		QueryWrapper<OrderPayRecord> wrapper = new QueryWrapper<>();
		wrapper.eq("id", 249L);
		Page<OrderPayRecord> pageList = orderPayRecordService.page(page,wrapper);
		pageList.getRecords().stream().forEach(record -> {
			System.out.println(JSON.toJSONString(record,true));
		});
	}
	
	
	/**
	 * mq消息测试
	 * @throws AmqpException
	 * @throws UnsupportedEncodingException
	 * @throws InterruptedException
	 */
//	@Test
	public void mqTest() throws AmqpException, UnsupportedEncodingException, InterruptedException {
//        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
//		rabbitTemplate.setExchange(QueueEnum.SINGLE_PHONE_MESSAGE_QUEUE.getExchange());
//		rabbitTemplate.setRoutingKey(QueueEnum.SINGLE_PHONE_MESSAGE_QUEUE.getName());
//        rabbitTemplate.convertAndSend(MessageBuilder.withBody("ssss".getBytes("UTF-8")).build());		
		
        // -------------延时发送-----------
		// 发送消息唯一识别表示
		String msgId = UUID.randomUUID().toString();
		CorrelationData correlationData = new CorrelationData(msgId);
        rabbitTemplate.convertAndSend(QueueEnum.SINGLE_PHONE_MESSAGE_TTL_QUEUE.getExchange(),
                QueueEnum.SINGLE_PHONE_MESSAGE_TTL_QUEUE.getRouteKey(),
                MessageBuilder.withBody("15213222722".getBytes("UTF-8")).build(),
                messageConf -> {
                    // 延时发送
                    messageConf.getMessageProperties().setExpiration(5000 + "");
                    return messageConf;
                },
                correlationData);
		Thread.sleep(200000);
		// -------------延时发送-----------
	}

	@Reference(version = "1.0.0")
	PayRpcService payRpcService;
	
	/**
	 * dubbo rpc测试
	 */
//	@Test
	public void testRpc() {
		Page<OrderPayRecord> page = payRpcService.queryPayOrder();
		
		System.out.println(JSON.toJSONString(page,true));	
	}
	
	@Autowired
	JedisService jedisService;
	
	/**
	 * 测试redis注解缓存、service缓存
	 */
//	@Test
	public void testRedis() {
		OrderPayRecord order = orderPayRecordService.getOrderPayRecordById(1L);
		System.out.println(JSON.toJSONString(order,true));
		jedisService.set("a", "2555", 60);
	}
	
	/**
	 * 测试自定义sql
	 */
//	@Test
	public void testMySql() {
		OrderPayRecord order = orderPayRecordService.selectByOrderNo("6589898998");
		System.out.println(JSON.toJSONString(order));
	}
	
	
}
