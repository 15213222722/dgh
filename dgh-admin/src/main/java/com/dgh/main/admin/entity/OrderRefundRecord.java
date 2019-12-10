package com.dgh.main.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 退款记录表
 * </p>
 *
 * @author dgh
 * @since 2019-12-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderRefundRecord对象", description="退款记录表")
public class OrderRefundRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "支付方式配置信息")
    private Long payConfigId;

    @ApiModelProperty(value = "退款单号")
    private String refundNo;

    @ApiModelProperty(value = "原订单号")
    private String orderNo;

    @ApiModelProperty(value = "三方订单号或者流水号")
    private String thirdOrderNo;

    @ApiModelProperty(value = "订单总金额，单位：分")
    private Integer totalAmount;

    @ApiModelProperty(value = "本次退款金额，单位：分")
    private Integer refundAmount;

    @ApiModelProperty(value = "退款原因")
    private String refundReason;

    @ApiModelProperty(value = "其他信息")
    private String extendsContent;

    @ApiModelProperty(value = "退款状态：0申请退款；1已退款；2退款失败")
    private Integer refundStatus;

    @ApiModelProperty(value = "退款状态描述")
    private String refundStatusDesc;

    @ApiModelProperty(value = "接收退款成功通知时间")
    private LocalDateTime refundFinishTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @Version
    private Integer version;


}
