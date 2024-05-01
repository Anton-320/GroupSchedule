package com.byshnev.groupschedule.components;

import com.byshnev.groupschedule.service.CounterService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggerAspect {
  private final Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

  @Pointcut("execution(public * com.byshnev.groupschedule.service.changes.*.*(..))"
      + "|| execution(public * com.byshnev.groupschedule.service.search.*.*(..))")
  public void servicesPointcut() {}

  @Before("servicesPointcut()")
  public void serviceLog(JoinPoint joinPoint) {
    logger.info("Service method was invoked - Class: {}, Method: {}, Args: {}",
                joinPoint.getTarget().getClass().getName(),
                joinPoint.getSignature().getName(),
                joinPoint.getArgs());
  }

  @Before("execution(* com.byshnev.groupschedule.exception.ExceptionsHandler.*(..))")
  public void errorLog(JoinPoint joinPoint) {
    logger.error("error: Exception handler {} with parameter(s) {} invoked",
                 joinPoint.getSignature().getName(),
                 joinPoint.getArgs());
  }

  @Before("execution(public * com.byshnev.groupschedule.service.search.*.*(..))")
  public void counterLog(JoinPoint joinPoint) {
    logger.info("Increment counter from {}.{}(). Current value of counter is {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                CounterService.increment());
  }
}
