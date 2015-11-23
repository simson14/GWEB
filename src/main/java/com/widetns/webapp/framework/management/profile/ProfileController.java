package com.widetns.webapp.framework.management.profile;


import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import com.widetns.webapp.framework.common.BaseObject;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/management/profile")
public class ProfileController extends BaseObject {

    @RequestMapping(value = "getProfile", method = {RequestMethod.GET})
    public void getProfile(HttpServletRequest req, HttpServletResponse res) {
        String currentProfile = currentProfile();
        info("currentProfile=" + currentProfile);
        req.setAttribute("currentProfile", currentProfile);
    }

    @RequestMapping(value = "changeProfile", method = {RequestMethod.GET})
    public void changeProfile(@RequestParam("profile") String profile, HttpServletRequest req, HttpServletResponse res) {

        System.setProperty(PROFILE, profile);

        ConfigurableApplicationContext rootContext = (ConfigurableApplicationContext) subCotext.getParent();
        rootContext.getEnvironment().setActiveProfiles(profile);
        rootContext.refresh();

        subCotext.getEnvironment().setActiveProfiles(profile);
        subCotext.refresh();

        LoggerContext logContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        logContext.reset();

        ContextInitializer ci = new ContextInitializer(logContext);
        try {
            ci.configureByResource(ci.findURLOfDefaultConfigurationFile(true));
        } catch (JoranException e) {
            e.printStackTrace();
        }


    }
}
