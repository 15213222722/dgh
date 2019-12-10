package com.dgh.main.admin.mapper;

import com.dgh.main.admin.entity.OrderPayRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 支付记录表 Mapper 接口
 * </p>
 *
 * @author dgh
 * @since 2019-12-10
 */
public interface OrderPayRecordMapper extends BaseMapper<OrderPayRecord> {

	OrderPayRecord selectByOrderNo(String orderNo);

}
