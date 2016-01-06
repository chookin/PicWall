package com.cmri.pic.wall.server.controller;

import cmri.utils.configuration.ConfigManager;
import cmri.utils.lang.JsonHelper;
import com.cmri.pic.wall.server.WebMvcConfig;
import com.cmri.pic.wall.server.domain.Pic;
import com.cmri.pic.wall.server.domain.ResponseMessage;
import com.cmri.pic.wall.server.helper.MultipartFileUploader;
import com.cmri.pic.wall.server.repository.PicRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zhuyin on 11/12/15.
 */
@Controller
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private PicRepository picRepository;

    /**
     * 上传图片文件
     * @param request http 请求
     * @param file 照片文件
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value="/upload", method = RequestMethod.POST)
    public ResponseMessage upload(HttpServletRequest request, @RequestParam(value = "img") MultipartFile file, String title) throws IOException{
        String filename = uploadImg(request, file);
        picRepository.save(Pic.newOne().setPath(filename).setTitle(title));
        return new ResponseMessage()
                .set("filename", filename)
                .set("url", WebMvcConfig.getUrl(filename))
                ;
    }

    /**
     * 上传图片文件，一次可以上传多张
     * @param request 请求
     * @param files 照片文件
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value="/uploadMultiple", method = RequestMethod.POST)
    public ResponseMessage upload(HttpServletRequest request, @RequestParam(value = "img") MultipartFile[] files) throws IOException{
        List<String> filenames = new ArrayList<>();
        for(MultipartFile file: files){
            String filename = uploadImg(request, file);
            filenames.add(filename);
        }
        List<Pic> pics = filenames.stream().map(filename -> Pic.newOne().setPath(filename)).collect(Collectors.toList());
        picRepository.save(pics);
        return new ResponseMessage()
                .set("count", String.valueOf(filenames.size()))
                .set("filenames", JsonHelper.toJson(filenames))
                ;
    }

    /**
     * 获取图片列表
     * @param since 指定起始时间，获取自该时刻至今所上传的图片
     */
    @ResponseBody
    @RequestMapping(value="/get", method = RequestMethod.GET)
    public ResponseMessage get(Long since){
        long myTime = since == null ? 0: since;
        List<Pic> pics = picRepository.findSince(new Timestamp(myTime));
        List<Map<String, String>> items = new ArrayList<>();
        for(Pic pic : pics){
            Map<String, String> item = new HashMap<>();
            item.put("title", pic.getTitle()==null?"":pic.getTitle());
            item.put("url", WebMvcConfig.getUrl(pic.getPath()));
            items.add(item);
        }
        return new ResponseMessage()
                .set("items", JsonHelper.toJson(items));
    }

    /**
     * 上传图片文件
     *
     * @param request http 请求
     * @param file 照片文件
     * @throws IOException
     */
    public static String uploadImg(HttpServletRequest request, MultipartFile file) throws IOException {
        String uploadPath = FilenameUtils.concat(ConfigManager.get("upload.basePath"), "image");
        return MultipartFileUploader.getInstance(request)
                .setUploadPath(uploadPath)
                .setDefaultExtension("png")
                .upload(file)
                ;
    }
}
