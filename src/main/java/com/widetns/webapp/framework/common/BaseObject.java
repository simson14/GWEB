package com.widetns.webapp.framework.common;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Configuration
@Profile("${spring.profiles.active}")
public class BaseObject implements BaseConstatns, Logger {

    protected static ModelAndView errorModel = new ModelAndView("/sample/general/error");
    @Autowired
    protected ConfigurableApplicationContext subCotext;
    @Autowired
    private PropertiesConfiguration configuration;
    private Logger llog = LoggerFactory.getLogger(this.getClass());

    public BaseObject() {
        setLogger(this.getClass());
    }


    private void setLogger(Class clazz) {
        this.llog = LoggerFactory.getLogger(clazz);
    }


    public String getConfig(String key) {
        return configuration.getString(key, "");
    }

    /**
     * logger Name Class의 Annotation 타입에 따라 Depth형 메시지 출력
     *
     * @param message
     * @return
     */
    private String getLoggerMessage(String message) {

        try {
            Class clazz = Class.forName(llog.getName());
            if (AnnotationUtils.findAnnotation(clazz, org.springframework.stereotype.Controller.class) != null) {
                return "\t" + LOG_PREFIX + message;
            } else if (AnnotationUtils.findAnnotation(clazz, org.springframework.stereotype.Service.class) != null) {
                return "\t\t" + LOG_PREFIX + message;
            } else if (AnnotationUtils.findAnnotation(clazz, org.springframework.stereotype.Repository.class) != null) {
                return "\t\t\t" + LOG_PREFIX + message;
            } else {
                return message;
            }
        } catch (ClassNotFoundException e) {
            return message;
        }
    }

    protected String getCurrentExecuteMethodName() {
        StackTraceElement trace = Thread.currentThread().getStackTrace()[2];
        return "Exception occurred " + trace.getClassName() + "." + trace.getMethodName() + "()";
    }

    public String getName() {
        return llog.getName();
    }

    public boolean isTraceEnabled() {
        return llog.isTraceEnabled();
    }

    public void trace(String s) {
        llog.trace(getLoggerMessage(s));
    }

    public void trace(String s, Object o) {
        llog.trace(getLoggerMessage(s), o);
    }

    public void trace(String s, Object o, Object o1) {
        llog.trace(getLoggerMessage(s), o, o1);
    }

    public void trace(String s, Object... objects) {
        llog.trace(getLoggerMessage(s), objects);
    }

    public void trace(String s, Throwable throwable) {
        llog.trace(getLoggerMessage(s), throwable);
    }

    public boolean isTraceEnabled(Marker marker) {
        return llog.isTraceEnabled(marker);
    }

    public void trace(Marker marker, String s) {
        llog.trace(marker, getLoggerMessage(s));
    }

    public void trace(Marker marker, String s, Object o) {
        llog.trace(marker, getLoggerMessage(s), o);
    }

    public void trace(Marker marker, String s, Object o, Object o1) {
        llog.trace(marker, getLoggerMessage(s), o, o1);
    }

    public void trace(Marker marker, String s, Object... objects) {
        llog.trace(marker, getLoggerMessage(s), objects);
    }

    public void trace(Marker marker, String s, Throwable throwable) {
        llog.trace(marker, getLoggerMessage(s), throwable);
    }

    public boolean isDebugEnabled() {
        return llog.isDebugEnabled();
    }

    public void debug(String s) {
        llog.debug(getLoggerMessage(s));
    }

    public void debug(String s, Object o) {
        llog.debug(getLoggerMessage(s), o);
    }

    public void debug(String s, Object o, Object o1) {
        llog.debug(getLoggerMessage(s), o, o1);
    }

    public void debug(String s, Object... objects) {
        llog.debug(getLoggerMessage(s), objects);
    }

    public void debug(String s, Throwable throwable) {
        llog.debug(getLoggerMessage(s), throwable);
    }

    public boolean isDebugEnabled(Marker marker) {
        return llog.isDebugEnabled(marker);
    }

    public void debug(Marker marker, String s) {
        llog.debug(marker, getLoggerMessage(s));
    }

    public void debug(Marker marker, String s, Object o) {
        llog.debug(marker, getLoggerMessage(s), o);
    }

    public void debug(Marker marker, String s, Object o, Object o1) {
        llog.debug(marker, getLoggerMessage(s), o, o1);
    }

    public void debug(Marker marker, String s, Object... objects) {
        llog.debug(marker, getLoggerMessage(s), objects);
    }

    public void debug(Marker marker, String s, Throwable throwable) {
        llog.debug(marker, getLoggerMessage(s), throwable);
    }

    public boolean isInfoEnabled() {
        return llog.isInfoEnabled();
    }

    public void info(String s) {
        llog.info(getLoggerMessage(s));
    }

    public void info(String s, Object o) {
        llog.info(getLoggerMessage(s), o);
    }

    public void info(String s, Object o, Object o1) {
        llog.info(getLoggerMessage(s), o, o1);
    }

    public void info(String s, Object... objects) {
        llog.info(getLoggerMessage(s), objects);
    }

    public void info(String s, Throwable throwable) {
        llog.info(getLoggerMessage(s), throwable);
    }

    public boolean isInfoEnabled(Marker marker) {
        return llog.isInfoEnabled(marker);
    }

    public void info(Marker marker, String s) {
        llog.info(marker, getLoggerMessage(s));
    }

    public void info(Marker marker, String s, Object o) {
        llog.info(marker, getLoggerMessage(s), o);
    }

    public void info(Marker marker, String s, Object o, Object o1) {
        llog.info(marker, getLoggerMessage(s), o, o1);
    }

    public void info(Marker marker, String s, Object... objects) {
        llog.info(marker, getLoggerMessage(s), objects);
    }

    public void info(Marker marker, String s, Throwable throwable) {
        llog.info(marker, getLoggerMessage(s), throwable);
    }

    public boolean isWarnEnabled() {
        return llog.isWarnEnabled();
    }

    public void warn(String s) {
        llog.warn(getLoggerMessage(s));
    }

    public void warn(String s, Object o) {
        llog.warn(getLoggerMessage(s), o);
    }

    public void warn(String s, Object... objects) {
        llog.warn(getLoggerMessage(s), objects);
    }

    public void warn(String s, Object o, Object o1) {
        llog.warn(getLoggerMessage(s), o, o1);
    }

    public void warn(String s, Throwable throwable) {
        llog.warn(getLoggerMessage(s), throwable);
    }

    public boolean isWarnEnabled(Marker marker) {
        return llog.isWarnEnabled(marker);
    }

    public void warn(Marker marker, String s) {
        llog.warn(marker, getLoggerMessage(s));
    }

    public void warn(Marker marker, String s, Object o) {
        llog.warn(marker, getLoggerMessage(s), o);
    }

    public void warn(Marker marker, String s, Object o, Object o1) {
        llog.warn(marker, getLoggerMessage(s), o, o1);
    }

    public void warn(Marker marker, String s, Object... objects) {
        llog.warn(marker, getLoggerMessage(s), objects);
    }

    public void warn(Marker marker, String s, Throwable throwable) {
        llog.warn(marker, getLoggerMessage(s), throwable);
    }

    public boolean isErrorEnabled() {
        return llog.isErrorEnabled();
    }

    public void error(String s) {
        llog.error(getLoggerMessage(s));
    }

    public void error(String s, Object o) {
        llog.error(getLoggerMessage(s), o);
    }

    public void error(String s, Object o, Object o1) {
        llog.error(getLoggerMessage(s), o, o1);
    }

    public void error(String s, Object... objects) {
        llog.error(getLoggerMessage(s), objects);
    }

    public void error(String s, Throwable throwable) {
        llog.error(getLoggerMessage(s), throwable);
    }

    public boolean isErrorEnabled(Marker marker) {
        return llog.isErrorEnabled(marker);
    }

    public void error(Marker marker, String s) {
        llog.error(marker, getLoggerMessage(s));
    }

    public void error(Marker marker, String s, Object o) {
        llog.error(marker, getLoggerMessage(s), o);
    }

    public void error(Marker marker, String s, Object o, Object o1) {
        llog.error(marker, getLoggerMessage(s), o, o1);
    }

    public void error(Marker marker, String s, Object... objects) {
        llog.error(marker, getLoggerMessage(s), objects);
    }

    public void error(Marker marker, String s, Throwable throwable) {
        llog.error(marker, getLoggerMessage(s), throwable);
    }

    protected String currentProfile() {
        String[] profiles = subCotext.getEnvironment().getActiveProfiles();

        if (profiles == null || profiles.length == 0) {
            profiles = subCotext.getEnvironment().getDefaultProfiles();
        }

        return profiles[0];
    }

    protected void checkErrorMessage(HttpServletRequest req) throws Exception {
        if (req.getAttribute("errMessage") != null) {
            throw new Exception("errorMessage");
        }
    }


}
