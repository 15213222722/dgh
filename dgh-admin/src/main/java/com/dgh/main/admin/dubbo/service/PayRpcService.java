package com.dgh.main.admin.dubbo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dgh.main.admin.entity.OrderPayRecord;

public interface PayRpcService{
	
	Page<OrderPayRecord> queryPayOrder();
	
	boolean updatePayOrderById(OrderPayRecord orderPayRecord);
	
}

