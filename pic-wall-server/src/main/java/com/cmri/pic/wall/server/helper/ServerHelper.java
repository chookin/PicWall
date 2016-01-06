package com.cmri.pic.wall.server.helper;

import cmri.utils.lang.TimeHelper;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by zhuyin on 11/14/15.
 */
public class ServerHelper {
    public static String getContextPath(HttpServletRequest request){
        return request.getSession().getServletContext().getRealPath("/");
    }

    public static String getDesc(HttpServletRequest request){
        return "request '" + request.getRequestURL() + "' " + ParasHelper.getParas(request).toString();
    }

    public static String getErrorDesc(String request, long id){
        return "Error for " + request + " at response " + id;
    }

    public static String getDateSubPath(String path){
        return FilenameUtils.concat(path, TimeHelper.toString(new Date(), "yyyyMMdd"));
    }

    /**
     * @param relPath the relative path, please not start with "/", or else the base path is no use.
     */
    public static String getUploadPath(HttpServletRequest request, String relPath){
        return FilenameUtils.concat(ServerHelper.getContextPath(request), relPath);
    }
}
