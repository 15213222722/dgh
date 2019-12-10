package com.dgh.main.admin.service;

import com.dgh.main.admin.entity.OrderPayRecord;

import java.io.Serializable;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 支付记录表 服务类
 * </p>
 *
 * @author dgh
 * @since 2019-12-10
 */
public interface OrderPayRecordService extends IService<OrderPayRecord> {
	OrderPayRecord getOrderPayRecordById(Serializable id);

	OrderPayRecord selectByOrderNo(String orderNo);
}
