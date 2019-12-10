package com.dgh.main.admin.config.mq;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/** 
* @author 作者 dgh
* @date 创建时间：2019年11月12日 下午3:34:39 
* 类说明 
*/
public class ImageMessageConverter implements MessageConverter {

    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        throw new MessageConversionException(" convert error ! ");
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        System.err.println("-----------Image MessageConverter----------");

        Object _extName = message.getMessageProperties().getHeaders().get("extName");
        String extName = _extName == null ? "png" : _extName.toString();

        byte[] body = message.getBody();
        String fileName = UUID.randomUUID().toString();
        //将接受到的图片放到该位置
        String path = "d://" + fileName + "." + extName;
        File f = new File(path);
        try {
            Files.copy(new ByteArrayInputStream(body), f.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }
}
