package com.dgh.main.admin.config.mq;

import java.io.File;
import java.util.Map;

public class MessageDelegate {


    //这个handleMessage方法名要根据org.springframework.amqp.rabbit.listener.adapter包下的
    //      MessageListenerAdapter.ORIGINAL_DEFAULT_LISTENER_METHOD的默认值来确定
    public void handleMessage(byte[] messageBody) {
    	System.out.println("默认方法, 消息内容:" + new String(messageBody));
    }

    public void consumeMessage(byte[] messageBody) {
    	System.out.println("字节数组方法, 消息内容:" + new String(messageBody));
    }

    //1.3 适配器方式.也可以添加一个转换器: 从字节数组转换为String
    public void consumeMessage(String messageBody) {
    	System.out.println("字符串方法, 消息内容:" + messageBody);
    }

    //        2 适配器方式: 我们的队列名称 和 方法名称 也可以进行一一的匹配
    public void method1(String messageBody) {
    	System.out.println("method1 收到消息内容:" + new String(messageBody));
    }
    public void method2(String messageBody) {
    	System.out.println("method2 收到消息内容:" + new String(messageBody));
    }

    public void consumeMessage(Map<?, ?> messageBody) {
    	System.out.println("map方法, 消息内容:" + messageBody);
    }

//    public void consumeMessage(Order order) {
//    	System.out.println();
//        log.info("order对象, 消息内容, id: " + order.getId() +
//                ", name: " + order.getName() +
//                ", content: "+ order.getContent());
//    }
//
//    public void consumeMessage(Packaged pack) {
//        log.info("package对象, 消息内容, id: " + pack.getId() +
//                ", name: " + pack.getName() +
//                ", content: "+ pack.getDescription());
//    }


    public void consumeMessage(File file) {
    	System.out.println("文件对象 方法, 消息内容:" + file.getName());
    }
}
