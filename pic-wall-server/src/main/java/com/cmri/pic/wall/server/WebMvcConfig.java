package com.cmri.pic.wall.server;

import cmri.utils.configuration.ConfigManager;
import com.cmri.pic.wall.server.interceptor.DebugInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;

/**
 * Created by zhuyin on 11/16/15.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    public static final String serverProtocol = ConfigManager.get("server.protocol");
    public static final String domain = ConfigManager.get("server.hostname");
    public static final int port = ConfigManager.getInt("server.port");
    public static final String baseUrl;
    static{
        baseUrl = serverProtocol + "://" + domain + ":" + port + "/";
    }

    /**
     * http://stackoverflow.com/questions/24661289/spring-boot-not-serving-static-content<br>
     * Unlike what the spring-boot states, to get my spring-boot jar to serve the content: I had to add specifically register my src/main/resources/static content
     */
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/"
            , "classpath:/resources/"
            , "classpath:/static/"
            , "classpath:/public/"
            , "file:///"+ConfigManager.get("chu.server.documentRoot") //serve my static resource from a file system folder outside my project
    };
    public static String getUrl(String path){
        return baseUrl + path;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if(ConfigManager.getBool("mode.dev")) {
            registry.addInterceptor(new DebugInterceptor());
        }
    }

    /**
     * By default Spring Boot will serve static content from a directory called /static (or /public or /resources or /META-INF/resources) in the classpath or from the root of the ServletContext. It uses the ResourceHttpRequestHandler from Spring MVC so you can modify that behavior by adding your own WebMvcConfigurerAdapter and overriding the addResourceHandlers method.
     */
    // equivalents for <mvc:resources/> tags
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
         registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }

    /**
     * To be able to serve static resources in Spring MVC application you need two XML-tags: <mvc:resources/> and <mvc:default-servlet-handler/>.
     * @param configurer
     */
    // equivalent for <mvc:default-servlet-handler/> tag
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public ChuServerProperties chuServerProperties(){
        return new ChuServerProperties();
    }

    @Bean
    public LocalEntityManagerFactoryBean entityManagerFactoryBean(){
        LocalEntityManagerFactoryBean bean = new LocalEntityManagerFactoryBean();
        bean.setPersistenceUnitName("snapshot");
        return bean;
    }

    /**
     * 配置document root dir，避免每次服务端启动都是基于随机路径。
     */
    @ConfigurationProperties(prefix = "chu.server")
    public static class ChuServerProperties
            implements EmbeddedServletContainerCustomizer
    {
        protected final Logger LOG = LoggerFactory.getLogger(getClass());
        private String documentRoot;
        public String getDocumentRoot(){
            return documentRoot;
        }
        public void setDocumentRoot(String documentRoot){
            LOG.info("set document root: "+documentRoot);
            this.documentRoot = documentRoot;
        }

        @Override
        public void customize(ConfigurableEmbeddedServletContainer container){
            if (getDocumentRoot() != null){
                container.setDocumentRoot(new File(getDocumentRoot()));
            }
        }
    }
}
