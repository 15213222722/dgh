package com.dgh.main.admin.controller;


import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dgh.main.admin.dto.OrderPayRecordDTO;
import com.dgh.main.admin.dubbo.service.PayRpcService;
import com.dgh.main.admin.entity.OrderPayRecord;
import com.dgh.main.admin.response.global.ResultBody;
import com.dgh.main.admin.service.impl.OrderPayRecordServiceImpl;

import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;

/**
 * <p>
 * 支付记录表 前端控制器
 * </p>
 *
 * @author dgh
 * @since 2019-12-10
 */
@RestController
@RequestMapping("/orderPayRecord")
public class OrderPayRecordController {
	
	private final Logger logger = LoggerFactory.getLogger(OrderPayRecordController.class);
	@Autowired
	private OrderPayRecordServiceImpl orderPayRecordService;
	
	@Reference(version = "1.0.0")
	PayRpcService payRpcService;
	
	@GetMapping(value = "test")
	@ResponseBody
	@GlobalTransactional
	public ResultBody test () {
		logger.info("seata分布式事务Id:{}", RootContext.getXID());
		//远程事务
		OrderPayRecord orderPayRecord = new OrderPayRecord();
		orderPayRecord.setId(1L);
		orderPayRecord.setOrderNo("888888a8ccccc");
		payRpcService.updatePayOrderById(orderPayRecord);
		
		//本地事务
		OrderPayRecord order = orderPayRecordService.getById(2L);
		orderPayRecord = new OrderPayRecord();
		orderPayRecord.setId(2L);
		orderPayRecord.setOrderNo("888888a8ddd");
		orderPayRecord.setVersion(order.getVersion());
		orderPayRecordService.updateById(orderPayRecord);
		//抛出异常
		int a = 1/0;
		return ResultBody.success();
	}
	
	@PostMapping(value = "test1")
	@ResponseBody
	public ResultBody test1 (@RequestBody @Validated OrderPayRecordDTO orderPayRecordDTO) {
		
		
		
		return ResultBody.success("更新成功");
	}
	
}
