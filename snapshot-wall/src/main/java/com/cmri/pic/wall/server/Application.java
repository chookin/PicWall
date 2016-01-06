package com.cmri.pic.wall.server;

import cmri.utils.lang.TimeHelper;
import cmri.utils.web.NetworkHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * http://docs.spring.io/spring-boot/docs/current/reference/html/howto-traditional-deployment.html<br>
 * Create a deployable war file:<br>
 *    The first step in producing a deployable war file is to provide a SpringBootServletInitializer subclass and override its configure method. This makes use of Spring Framework’s Servlet 3.0 support and allows you to configure your application when it’s launched by the servlet container. Typically, you update your application’s main class to extend SpringBootServletInitializer:
 *
 */
@SpringBootApplication
public class Application {

    static {
        // configure log4j to log to custom file at runtime. In the java program directly by setting a system property (BEFORE you make any calls to log4j).
        try {
            String suffix = InetAddress.getLocalHost().getHostName() + "-" + TimeHelper.toString(new Date(), "yyyyMMddHH");
            String actionName = System.getProperty("action");
            System.setProperty("hostname.time", actionName == null ? suffix : (actionName + "-" + suffix));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        NetworkHelper.setDefaultProxy();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
