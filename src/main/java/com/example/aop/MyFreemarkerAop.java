package com.example.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xuan
 * @create 2018-04-15 11:41
 **/
@Aspect
@Component
public class MyFreemarkerAop {
    @Pointcut("execution(public * com.example.controller.FreemarkerTestController.*(..))&&" +
            "!execution(public * com.example.controller.FreemarkerTestController.test1(..))")
    public void myPointCut(){}

    @Before("myPointCut()")
    public void doAop(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        System.out.println(request.getRemoteAddr());
    }
}
