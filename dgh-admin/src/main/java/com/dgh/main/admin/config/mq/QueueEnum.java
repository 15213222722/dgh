package com.dgh.main.admin.config.mq;
/** 
* @author 作者 dgh
* @date 创建时间：2019年11月13日 下午4:23:11 
* 类说明 
*/
public enum QueueEnum {
    /**
     * 单个短信发送队列，主要的发送消息队列
     */
    SINGLE_PHONE_MESSAGE_QUEUE("single.phone.message.direct", "single.phone.message", "single.phone.message"),

    /**
     * 单个短信发送ttl队列，用于延时发送，通过死信交换方式实现，首先将消息发送到延时队列，消息到期后，发送到绑定的死信队列(这里即正式发送消息的队列)
     */
    SINGLE_PHONE_MESSAGE_TTL_QUEUE("single.phone.message.direct", "single.phone.message.ttl", "single.phone.message.ttl"),

    /**
     * 单个短信发送dead队列，消息发送失败后，发送到dead队列，记录失败数据
     */
    SINGLE_PHONE_MESSAGE_DEAD_QUEUE("single.phone.message.direct", "single.phone.message.dead", "single.phone.message.dead");

    QueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }

    /**
     * 交换名称
     */
    private String exchange;

    /**
     * 队列名称
     */
    private String name;

    /**
     * 路由键
     */
    private String routeKey;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange == null ? null : exchange.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey == null ? null : routeKey.trim();
    }
}
