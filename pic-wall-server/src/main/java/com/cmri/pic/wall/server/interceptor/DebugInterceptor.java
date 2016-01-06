package com.cmri.pic.wall.server.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by zhuyin on 11/11/15.
 */
public class DebugInterceptor extends HandlerInterceptorAdapter {
     protected final Logger LOG = LoggerFactory.getLogger(getClass());
    // 重写 preHandle()方法，在业务处理器处理请求之前对该请求进行拦截处理
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        StringBuilder strb = new StringBuilder()
                .append("Receive request: ")
                .append(request.getMethod())
                .append("\t")
                .append(request.getRequestURL())
                .append("\t")
                .append("paras: ")
                .append("\n")
                ;
        for(Map.Entry<String, String[]> entry: request.getParameterMap().entrySet()){
            strb.append("\t")
                    .append(entry.getKey())
                    .append(": ")
                    .append("[")
                    .append(StringUtils.join(entry.getValue(), ","))
                    .append("]")
                    .append("\n");
        }
        LOG.debug(strb.toString());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
}
