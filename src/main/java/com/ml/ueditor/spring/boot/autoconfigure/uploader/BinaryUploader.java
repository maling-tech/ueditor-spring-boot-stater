package com.ml.ueditor.spring.boot.autoconfigure.uploader;

import com.ml.ueditor.spring.boot.autoconfigure.ActionConfig;
import com.ml.ueditor.spring.boot.autoconfigure.define.AppInfo;
import com.ml.ueditor.spring.boot.autoconfigure.define.BaseState;
import com.ml.ueditor.spring.boot.autoconfigure.define.FileType;
import com.ml.ueditor.spring.boot.autoconfigure.define.State;
import com.ml.ueditor.spring.boot.autoconfigure.manager.IUeditorFileManager;
import com.ml.ueditor.spring.boot.autoconfigure.util.PathFormat;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class BinaryUploader {

    public static final State save(IUeditorFileManager fileManager, HttpServletRequest request,
                                   ActionConfig conf) {
        if (!(request instanceof MultipartHttpServletRequest)) {
            return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        String fieldName = conf.getFieldName();
        MultipartFile file = multipartRequest.getFile(fieldName);

        String savePath = conf.getSavePath();
        String originFileName = file.getOriginalFilename();
        String suffix = FileType.getSuffixByFilename(originFileName);

        originFileName = originFileName.substring(0, originFileName.length() - suffix.length());
        savePath = savePath + suffix;
        long maxSize = conf.getMaxSize();

        if (!validType(suffix, conf.getAllowFiles())) {
            return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
        }
        savePath = PathFormat.parse(savePath, originFileName);
        String rootPath = conf.getRootPath();

        try (InputStream is = file.getInputStream()) {
            State storageState = fileManager.saveFile(is, rootPath, savePath, maxSize);

            if (storageState.isSuccess()) {
                storageState.putInfo("url", PathFormat.format(savePath));
                storageState.putInfo("type", suffix);
                storageState.putInfo("original", originFileName + suffix);
            }
            return storageState;
        } catch (IOException e) {
            return new BaseState(false, AppInfo.IO_ERROR);
        }

    }

    private static boolean validType(String type, List<String> allowTypes) {
        return allowTypes.contains(type);
    }
}
