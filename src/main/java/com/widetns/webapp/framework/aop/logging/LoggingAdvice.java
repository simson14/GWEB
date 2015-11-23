package com.widetns.webapp.framework.aop.logging;

import com.widetns.webapp.framework.common.BaseConstatns;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class LoggingAdvice implements BaseConstatns {

    public LoggingAdvice() {
        super();
    }

    /**
     * 수행 시간 출력
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    public Object loggingExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Logger llog = LoggerFactory.getLogger(proceedingJoinPoint.getSignature().getDeclaringType());
        if (isController(proceedingJoinPoint)) {
            llog.info("Request Start----------------------------------------------------------------------------");
        }
        llog.info(getLoggingPrefix(proceedingJoinPoint) + proceedingJoinPoint.getSignature().getName() + "() is started");
        loggingArguments(llog, proceedingJoinPoint);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object obj = proceedingJoinPoint.proceed();
        stopWatch.stop();
        llog.info(getLoggingPrefix(proceedingJoinPoint) + proceedingJoinPoint.getSignature().getName() + "() {executed  in " + stopWatch.getTotalTimeMillis() + " msec}");
        if (isController(proceedingJoinPoint)) {
            llog.info("Request End------------------------------------------------------------------------------");
        }
        return obj;
    }

    /**
     * 수행도중 에러 발생시 에러로그 출력
     *
     * @param joinPoint : 수행 jointPoint
     * @param exception : 발생 exception
     */
    public void loggingThrowing(JoinPoint joinPoint, Exception exception) {
        Logger llog = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType());
        llog.error("Exception occured", exception);
    }

    /**
     * Controller 여부 확인
     *
     * @param joinPoint
     * @return
     */
    private boolean isController(JoinPoint joinPoint) {
        return (joinPoint.getSignature().getDeclaringType().getAnnotation(Controller.class) != null);
    }

    /**
     * Annotation 타입에 따른 로그 prefix 선택
     *
     * @param joinPoint
     * @return
     */
    private String getLoggingPrefix(JoinPoint joinPoint) {
        if (joinPoint.getSignature().getDeclaringType().getAnnotation(Controller.class) != null) {
            return "\t" + LOG_PREFIX;
        } else if (joinPoint.getSignature().getDeclaringType().getAnnotation(Service.class) != null) {
            return "\t\t" + LOG_PREFIX;
        } else if (joinPoint.getSignature().getDeclaringType().getAnnotation(Repository.class) != null) {
            return "\t\t\t" + LOG_PREFIX;
        } else {
            return LOG_PREFIX;
        }
    }

    /**
     * 파라미터 정보 출력
     *
     * @param llog
     * @param joinPoint
     * @throws Exception
     */
    private void loggingArguments(Logger llog, JoinPoint joinPoint) throws Exception {

        boolean isExecuted = false;
        StringBuilder sb = new StringBuilder(100);
        Object[] objs = joinPoint.getArgs();
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof HttpServletRequest) {
                if (!isExecuted) {
                    for (Object key : ((HttpServletRequest) arg).getParameterMap().keySet()) {
                        sb.append(key).append('=').append(((HttpServletRequest) arg).getParameter(key.toString())).append(", ");
                    }
                }
                if (((HttpServletRequest) arg).getAttribute("errMessage") != null) {
                    llog.error(getLoggingPrefix(joinPoint) + "Error Message{" + ((HttpServletRequest) arg).getAttribute("errMessage") + "}");
                }
            } else if (arg instanceof LinkedHashMap) {
                isExecuted = true;
                for (Object key : ((LinkedHashMap) arg).keySet()) {
                    sb.append(key).append('=').append(((LinkedHashMap) arg).get(key)).append(", ");
                }
            } else if (arg instanceof HashMap) {
                for (Object key : ((HashMap) arg).keySet()) {
                    sb.append(key).append('=').append(((HashMap) arg).get(key)).append(", ");
                }
            }
        }
        if (!sb.toString().equals("")) {
            llog.info(getLoggingPrefix(joinPoint) + "Parameters{" + sb.toString().substring(0, sb.toString().length() - 2) + '}');
        }
    }
}
