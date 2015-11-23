package com.widetns.webapp.framework.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class StartupListener implements ServletContextListener {

    private static final Logger llog = LoggerFactory.getLogger(StartupListener.class);

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        llog.info("StartupListener is started");
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        llog.info("StartupListener is ended");
    }
}
