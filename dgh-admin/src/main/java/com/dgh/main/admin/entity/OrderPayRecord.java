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
 * 支付记录表
 * </p>
 *
 * @author dgh
 * @since 2019-12-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderPayRecord对象", description="支付记录表")
public class OrderPayRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "支付方式配置信息")
    private Long payConfigId;

    @ApiModelProperty(value = "支付来源：APP、PC")
    private String payWay;

    @ApiModelProperty(value = "支付机构：alipay、weChat")
    private String payType;

    @ApiModelProperty(value = "支付方式：weChat-JSAPI、weChat-NATIVE、weChat-APP、weChat-MWEB、alipay-WAP、alipay-APP、alipay-PAGE")
    private String payMethod;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "三方订单号或者流水号")
    private String thirdOrderNo;

    @ApiModelProperty(value = "销售产品码，与支付宝签约的产品码名称")
    private String productCode;

    @ApiModelProperty(value = "订单总金额，单位：分")
    private Integer totalAmount;

    @ApiModelProperty(value = "订单标题")
    private String orderTitle;

    @ApiModelProperty(value = "订单描述")
    private String orderDesc;

    @ApiModelProperty(value = "其他信息")
    private String extendsContent;

    @ApiModelProperty(value = "支付状态：0未支付；1已支付；2支付失败；3申请退款；4已退款")
    private Integer payStatus;

    @ApiModelProperty(value = "支付状态描述")
    private String payStatusDesc;

    @ApiModelProperty(value = "接收支付成功通知时间")
    private LocalDateTime payFinishTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @Version
    private Integer version;


}
