package com.adexchange.adsponsor.util;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LogAspect {
//    @Pointcut(value = "execution(* com.adexchange.adsponsor.controller.*.openRTB(..))")
//    public void pointCut(){}
//
//    @Before(value = "pointCut()")
//    public void RequestParams(JoinPoint joinPoint) {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//        System.out.println(request.getRequestURL());
//        System.out.println(request.getMethod());
//        System.out.println(JSON.toJSONString(joinPoint.getArgs()));
//    }
}
