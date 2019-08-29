package com.daxuepai.gaoxiao.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Aspect
@Component
public class LogAspect {
    public static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(public * com.daxuepai.gaoxiao.controller..*.*(..))")
    public void weblog(){}

    @Before("weblog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable{
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("url=" + request.getRequestURL() + "&");
        request.getParameterMap();
        stringBuilder.append("params=" + getAllRequestParams(request));
        logger.info(stringBuilder.toString());
    }

    public static StringBuilder getAllRequestParams(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder("[ ");
        Map<String, String[]> map = request.getParameterMap();
        if (map != null) {
            Set<Map.Entry<String, String[]>> set = map.entrySet();
            if (set != null) {
                Iterator var4 = set.iterator();

                while(var4.hasNext()) {
                    Map.Entry<String, String[]> e = (Map.Entry)var4.next();
                    sb.append((String)e.getKey());
                    sb.append(" : ");
                    sb.append(((String[])e.getValue())[0]);
                    sb.append(" , ");
                }
            }
        }

        sb.append(" ]");
        return sb;
    }
}
