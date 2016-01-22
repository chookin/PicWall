package cmri.snapshot.wall.server.helper;

import cmri.utils.io.FileHelper;
import cmri.utils.lang.Pair;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by zhuyin on 11/13/15.
 */
public class MultipartFileUploader {
    private final HttpServletRequest request;
    private String uploadPath;
    private String defaultExtension;
    protected MultipartFileUploader(HttpServletRequest request){
        this.request = request;
    }
    public static MultipartFileUploader getInstance(HttpServletRequest request){
        return new MultipartFileUploader(request);
    }
    /**
     * 随机生成文件名
     */
    static String genFilename(){
        return UUID.randomUUID().toString();
    }
    public String getUploadPath() {
        return uploadPath;
    }

    public MultipartFileUploader setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
        return this;
    }

    public String getDefaultExtension(){
        return this.defaultExtension;
    }
    public MultipartFileUploader setDefaultExtension(String extension){
        this.defaultExtension = extension;
        return this;
    }

    /**
     * Update MultipartFile to server，the stored path is：
        request.getSession().getServletContext().getRealPath("/") +　$uploadPath + TimeHelper.toString(new Date(), "yyyyMMdd")；
     And the stored file name is：
        uuid.rand + "." + $extension

     default $uploadPath is ConfigManager.get("upload.uploadPath");
     $extension is parsed from source multipartfile name, if fail to parse, then set to defaultExtension, which is set by call method setDefaultExtension().
     * @param file MultipartFile
     * @return Pair of file original name and file saved path. Saved path is relative to server context path.
     * @throws IOException
     */
    public Pair<String,String> upload(MultipartFile file) throws IOException {
        String imgPath = ServerHelper.getDateSubPath(uploadPath);
        String fullPath = ServerHelper.getUploadPath(request, imgPath);
        FileHelper.mkdirs(fullPath);
        String originalName = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalName);
        if(extension.isEmpty()){
            extension = defaultExtension;
        }
        String myName = genFilename() + "." + extension;
        file.transferTo(new File(FilenameUtils.concat(fullPath, myName)));
        return new Pair<>(originalName, FilenameUtils.concat(imgPath, myName));
    }
}
