package com.dgh.main.admin.dubbo.service.impl;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dgh.main.admin.dubbo.service.PayRpcService;
import com.dgh.main.admin.entity.OrderPayRecord;
import com.dgh.main.admin.service.impl.OrderPayRecordServiceImpl;

@Service(version = "1.0.0",interfaceClass = PayRpcService.class) 
public class PayRpcServiceImpl implements PayRpcService{
	
	@Autowired
	private OrderPayRecordServiceImpl orderPayRecordService;
	
	@Override
	public Page<OrderPayRecord> queryPayOrder() {
		Page<OrderPayRecord> page = new Page<>(1, 10);
		page.addOrder(OrderItem.desc("id"));
		QueryWrapper<OrderPayRecord> wrapper = new QueryWrapper<>();
//		wrapper.eq("id", 249L);
		Page<OrderPayRecord> pageList = orderPayRecordService.page(page,wrapper);
		return pageList;
	}

	@Override
	public boolean updatePayOrderById(OrderPayRecord orderPayRecord) {
		OrderPayRecord order = orderPayRecordService.getById(orderPayRecord.getId());
		orderPayRecord.setVersion(order.getVersion());
		return orderPayRecordService.updateById(orderPayRecord);
	}
	
	
}
