package com.daxuepai.gaoxiao.aspect;


import com.daxuepai.gaoxiao.model.HostHolder;
import com.daxuepai.gaoxiao.model.IPRecord;
import com.daxuepai.gaoxiao.model.Monitor;
import com.daxuepai.gaoxiao.model.User;
import com.daxuepai.gaoxiao.service.IPRecordService;
import com.daxuepai.gaoxiao.service.MonitorService;
import com.daxuepai.gaoxiao.util.StatusCode;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.daxuepai.gaoxiao.util.IPUtils.getIpAddress;

@Aspect
@Component
public class LogAspect {
    public static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    MonitorService monitorService;

    @Autowired
    IPRecordService ipRecordService;


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

    @Around("execution(public * com.daxuepai.gaoxiao.controller..*.*(..))")
    public Object countFunctionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        long startTime = System.currentTimeMillis();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Object object = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long takeTime = endTime - startTime;

        Monitor monitor = new Monitor();
        monitor.setCreateTime(new Date());
        User user = hostHolder.getUser();
        if(user != null){
            monitor.setUserId(user.getId());
        }
        monitor.setTakeTime(takeTime);
        monitor.setUrl(request.getRequestURL().toString());
        try {
            int count = monitorService.insert(monitor);
            if (count == 0) {
                logger.error(StatusCode.INSERT_MONITOR_FAIL.toString());
            }
        }catch (Exception e){
            logger.error(StatusCode.INSERT_MONITOR_EXCEPTION.toString());
            logger.error("", e);
        }
        return object;
    }

    @Around("execution(public * com.daxuepai.gaoxiao.controller..*.*(..))")
    public Object countIpRecord(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = getIpAddress(request);
        Date now = new Date();
        User user = hostHolder.getUser();
        int userId = 0;
        if(user != null){
            userId = user.getId();
        }
        IPRecord ipRecord = new IPRecord();
        ipRecord.setDate(now);
        ipRecord.setIp(ip);
        ipRecord.setUserId(userId);
        ipRecordService.recordIp(ipRecord);
        Object object = proceedingJoinPoint.proceed();
        return object;
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
