package com.zh.cloud.admin.handler;

import com.ch.e.PubError;
import com.ch.result.Result;
import com.zh.cloud.admin.common.exception.ServiceException;
import com.zh.cloud.admin.model.BaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * MVC异常拦截器
 *
 * @author rewerma 2019-07-13 下午05:12:16
 * @version 1.0.0
 */
@ControllerAdvice(annotations = ResponseBody.class)
public class CustomExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    /**
     * 通用异常处理
     *
     * @param e 异常
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = Exception.class)
    public Result<?> commonExceptionHandle(Exception e) {
        if (e instanceof ServiceException) {
            logger.error(e.getMessage());
        } else {
            logger.error(e.getMessage(), e);
        }
        return Result.error(PubError.UNKNOWN);
    }
}
