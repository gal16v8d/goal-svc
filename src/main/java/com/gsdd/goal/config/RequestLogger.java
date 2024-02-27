package com.gsdd.goal.config;

import com.gsdd.goal.GoalApplication;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class RequestLogger {

  @Around("execution(* " + GoalApplication.BASE_PACKAGE + "controller.*.*(..))")
  public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
    Object o = null;
    String methodName = joinPoint.getSignature().getName();
    long startTime = System.nanoTime();
    log.info("Before: {}", methodName);
    try {
      o = joinPoint.proceed();
    } catch (Exception e) {
      throw e;
    } finally {
      log.info("After: {}", methodName);
      long timeTaken = System.currentTimeMillis() - startTime;
      log.info(
          "Execution of {} tooks {} ms",
          methodName,
          TimeUnit.MILLISECONDS.convert(timeTaken, TimeUnit.NANOSECONDS));
    }
    return o;
  }
}
