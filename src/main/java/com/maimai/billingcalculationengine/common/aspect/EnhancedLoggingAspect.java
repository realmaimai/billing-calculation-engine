package com.maimai.billingcalculationengine.common.aspect;

import com.maimai.billingcalculationengine.common.annotations.TrackExecution;
import com.maimai.billingcalculationengine.common.enums.Layer;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * Aspect for automatically filling timestamps in entities
 * Works with @TimeStamp annotation to set createTime and updateTime
 */
@Aspect
@Component
@Slf4j
public class EnhancedLoggingAspect {

    @Pointcut("@annotation(com.maimai.billingcalculationengine.common.annotations.TrackExecution)")
    public void trackExecutionPointcut() {}

    @Around("trackExecutionPointcut()")
    public Object logExecutionTime(ProceedingJoinPoint jp) throws Throwable {
        MethodSignature signature = (MethodSignature) jp.getSignature();
        TrackExecution annotation = signature.getMethod().getAnnotation(TrackExecution.class);
        Layer layer = annotation.value();

        String methodPath = jp.getSignature().getDeclaringTypeName() + "." + jp.getSignature().getName();

        long startTime = System.currentTimeMillis();

        log.info("[{}] Enter: {}", layer, methodPath);

        // execute method
        Object result = jp.proceed();

        long executionTime = System.currentTimeMillis() - startTime;

        log.info("[{}] Exit: {} Time taken: {}ms", layer, methodPath, executionTime);

        return result;
    }
}
