package com.dgh.main.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 支付配置表
 * </p>
 *
 * @author dgh
 * @since 2019-12-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SystemPayConfig对象", description="支付配置表")
public class SystemPayConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "内部业务code，生成规则8位数字自增，10000001")
    private String mchCode;

    @ApiModelProperty(value = "商户APPID")
    private String appid;

    @ApiModelProperty(value = "商户号")
    private String mchId;

    @ApiModelProperty(value = "证书路径")
    private String certPath;

    @ApiModelProperty(value = "商户key")
    private String paternerKey;

    @ApiModelProperty(value = "服务器ip")
    private String clientIp;

    @ApiModelProperty(value = "支付回调url")
    private String payNotifyUrl;

    @ApiModelProperty(value = "退款回调url")
    private String refundNotifyUrl;

    @Version
    private Integer version;


}
