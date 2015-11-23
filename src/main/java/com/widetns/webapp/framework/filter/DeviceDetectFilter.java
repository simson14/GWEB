package com.widetns.webapp.framework.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

class DeviceDetectFilter implements Filter {

    private static final Logger llog = LoggerFactory.getLogger(DeviceDetectFilter.class);

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        llog.info("doFilter executed");

    }

    public void destroy() {

    }
}
