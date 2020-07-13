package com.itomelet.blog.handler;

import com.itomelet.blog.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //标识这个方法是做异常处理
    @ExceptionHandler(Exception.class) //表示拦截所有异常
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) throws Exception {
        logger.error("Request URL : {} , Exception:{}", request.getRequestURL(), e.toString());
        //存在了指定的状态
        if (AnnotationUtils.findAnnotation(NotFoundException.class, ResponseStatus.class) != null) {
            throw e;
        }
        ModelAndView mv = new ModelAndView();
        mv.addObject("url", request.getRequestURL());
        mv.addObject("exception", e);
        mv.setViewName("error/error");
        return mv;
    }
}
