package com.dgh.main.admin.exception.global;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import com.dgh.main.admin.response.global.CommonEnum;
import com.dgh.main.admin.response.global.ResultBody;

import lombok.extern.slf4j.Slf4j;

/**
 * @author dgh
 * @ClassName: GlobalExceptionHandler
 * @Description: 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler({ConstraintViolationException.class,MethodArgumentNotValidException.class,ValidationException.class})
    public ResultBody handleConstraintViolationException(ConstraintViolationException e) {
        log.error("参数校验失败：{}", e);
        return ResultBody.error(CommonEnum.PARAM_VALIDATION);
    }

    /**
     * 处理404异常
     * @param e
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResultBody handlerNoFoundException(Exception e) {
        log.error("路径不存在，请检查路径是否正确:{}", e);
        return ResultBody.error(CommonEnum.NOT_FOUND);
    }
    
    /**
     * 处理数据库唯一健值重复异常
     * @param e
     * @return
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResultBody handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("发生健值重复！原因是：{}",e);
        return ResultBody.error(CommonEnum.REPEAT_KEY);
    }
 
    
    /**
     * 处理自定义的业务异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public ResultBody bizExceptionHandler(HttpServletRequest req, BizException e){
        log.error("发生业务异常！原因是：{}",e.getErrorMsg());
        return ResultBody.error(CommonEnum.BODY_NOT_MATCH);
    }
 
 
    /**
     * 处理空指针的异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResultBody exceptionHandler(HttpServletRequest req, NullPointerException e){
        log.error("发生空指针异常！原因是:",e);
        return ResultBody.error(CommonEnum.BODY_NOT_MATCH);
    }
 
    /**
     * 处理请求方法不支持的异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResultBody exceptionHandler(HttpServletRequest req, HttpRequestMethodNotSupportedException e){
        log.error("发生请求方法不支持异常！原因是:",e);
        return ResultBody.error(CommonEnum.REQUEST_METHOD_SUPPORT_ERROR);
    }
 
    /**
     * 处理其他异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultBody exceptionHandler(HttpServletRequest req, Exception e){
        log.error("未知异常！原因是:",e);
        return ResultBody.error(CommonEnum.INTERNAL_SERVER_ERROR);
    }
    
    
    
}