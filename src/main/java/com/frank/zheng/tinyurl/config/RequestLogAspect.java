package com.frank.zheng.tinyurl.config;

import com.alibaba.fastjson.JSON;
import com.frank.zheng.tinyurl.entity.RequestLog;
import com.frank.zheng.tinyurl.service.KafkaService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
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

@Aspect
@Component
public class RequestLogAspect {
    private static Logger logger = LoggerFactory.getLogger(RequestLog.class);

    private ThreadLocal<RequestLog> logThreadLocal = new ThreadLocal<>();

    @Autowired
    private KafkaService kafkaService;

    @Pointcut("execution(* com.frank.zheng.tinyurl.web.controller..*.*(..))")
    public void pointcut() {
        logger.info("intercept request start");
    }

    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            logger.error("Failed to get request attributes");
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        String beanName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        String uri = request.getRequestURI();
        Object[] paramsArray = joinPoint.getArgs();
        logger.info("intercept request params: uri={}, method={}, request={}",
                uri,
                methodName,
                paramsArray);
        RequestLog requestLog = new RequestLog();
        requestLog.setUri(uri);
        requestLog.setRequestMethod(request.getMethod());
        requestLog.setBeanName(beanName);
        requestLog.setMethodName(methodName);
        requestLog.setRequestParams(argsArrayToString(paramsArray));
        requestLog.setTimestamp(new Date()); //here json will use millis by default, may need convert to string
        logThreadLocal.set(requestLog);

    }

    @AfterReturning(returning = "result", pointcut = "pointcut()")
    public void doAfterReturning(Object result) {
        try {
            // When request handled by the mvc controller
            // Get the log from the thread local and send to kafka
            RequestLog log = logThreadLocal.get();
            if (null != log) {
                log.setResponseData(JSON.toJSONString(result));
                int duration = (int)((System.currentTimeMillis() - log.getTimestamp().getTime()) / 1000);
                log.setDuration(duration);
                kafkaService.publishRequestLog(log);
            }
        } catch (Exception e) {
            logger.error("Failed to log the response, ", e);
        } finally {
            // cleanup
            logThreadLocal.remove();
        }
    }

    private String argsArrayToString(Object[] paramsArray) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null && paramsArray.length > 0) {
            for (int i = 0; i < paramsArray.length; i++) {
                Object jsonObj = JSON.toJSON(paramsArray[i]);
                params.append(jsonObj.toString());
                params.append(" ");
            }
        }
        return params.toString().trim();
    }

}
