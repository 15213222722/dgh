package com.dgh.main.admin.log.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * controller请求日志，只在调式dev环境生效
 * @author dgh
 *
 */
@Aspect
@Component
@Slf4j
@Profile(value ="dev")
public class LogAop {
	
    ThreadLocal<Long> startTime = new ThreadLocal<Long>();

    @Autowired
    private ObjectMapper mapper;
    
    //这个表达式的意思是，HelloLogCotroller这个controller下所有的方法都会切入
    @Pointcut("execution(* com.dgh.main.admin.controller.*.*(..))")
    public void setLogger(){}
 
    @Before("setLogger()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.info("-----begin------");
        // 记录下请求内容
        log.info("请求url : {}",request.getRequestURL().toString());
        log.info("请求类型 : {}",request.getMethod());
        log.info("ip :{} ",request.getRemoteAddr());
        log.info("接口包路径 : {}",joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("请求参数: {}",mapper.writeValueAsString(request.getParameterMap()));
    }
 
    @AfterReturning(returning = "ret", pointcut = "setLogger()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        log.info("response : {}",ret);
        log.info("spend time : {}ms",(System.currentTimeMillis() - startTime.get()));
        log.info("-----end------");
    }
}
