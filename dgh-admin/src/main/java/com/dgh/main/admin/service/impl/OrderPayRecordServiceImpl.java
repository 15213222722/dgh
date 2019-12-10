package com.dgh.main.admin.service.impl;

import java.io.Serializable;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dgh.main.admin.entity.OrderPayRecord;
import com.dgh.main.admin.mapper.OrderPayRecordMapper;
import com.dgh.main.admin.service.OrderPayRecordService;

/**
 * <p>
 * 支付记录表 服务实现类
 * </p>
 *
 * @author dgh
 * @since 2019-12-10
 */
@Service
public class OrderPayRecordServiceImpl extends ServiceImpl<OrderPayRecordMapper, OrderPayRecord> implements OrderPayRecordService {
	@Override
	@Cacheable(cacheNames = "getOrderPayRecordById",key = "#id", unless="#result == null")
	public OrderPayRecord getOrderPayRecordById(Serializable id) {
		return getBaseMapper().selectById(id);
	}
	
	@Override
	public OrderPayRecord selectByOrderNo(String orderNo) {
		return getBaseMapper().selectByOrderNo(orderNo);
	}
}
