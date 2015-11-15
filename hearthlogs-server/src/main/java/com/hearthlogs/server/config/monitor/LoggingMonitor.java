package com.hearthlogs.server.config.monitor;

import com.hearthlogs.server.analytics.JGoogleAnalyticsTracker;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingMonitor {

    @Autowired
    private JGoogleAnalyticsTracker analyticsTracker;

    @Around("execution(* com.hearthlogs.server.controller.HomeController.home(..))")
    public Object home(ProceedingJoinPoint pjp) throws Throwable {
        Object retVal = pjp.proceed();
        return retVal;
    }
}