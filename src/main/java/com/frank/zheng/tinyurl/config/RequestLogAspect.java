package com.frank.zheng.tinyurl.config;

import com.alibaba.fastjson.JSON;
import com.frank.zheng.tinyurl.entity.RequestLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class RequestLogAspect {
    private static Logger logger = LoggerFactory.getLogger(RequestLog.class);

    private ThreadLocal<RequestLog> logThreadLocal = new ThreadLocal<>();

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
        requestLog.setRequestMethod(methodName);
        requestLog.setBeanName(beanName);
        requestLog.setRequestParams(argsArrayToString(paramsArray));
        logThreadLocal.set(requestLog);

    }

    @AfterReturning(returning = "result", pointcut = "pointcut()")
    public void doAfterReturning(Object result) {
        try {
            // 处理完请求，从线程变量中获取日志数据，并记录到db
            RequestLog log = logThreadLocal.get();
            if (null != log) {
                log.setResponseData(JSON.toJSONString(result));
                //TODO: publish the log to kafka
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
