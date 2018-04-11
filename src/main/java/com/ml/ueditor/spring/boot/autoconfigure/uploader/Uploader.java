package com.ml.ueditor.spring.boot.autoconfigure.uploader;


import com.ml.ueditor.spring.boot.autoconfigure.ActionConfig;
import com.ml.ueditor.spring.boot.autoconfigure.define.State;
import com.ml.ueditor.spring.boot.autoconfigure.manager.IUeditorFileManager;

import javax.servlet.http.HttpServletRequest;

public class Uploader {
    private HttpServletRequest request;
    private ActionConfig conf;

    public Uploader(HttpServletRequest request, ActionConfig conf) {
        this.request = request;
        this.conf = conf;
    }

    public final State doExec(IUeditorFileManager fileManager) {
        String filedName = conf.getFieldName();
        State state;
        if (conf.isBase64()) {
            state = Base64Uploader.save(fileManager, request.getParameter(filedName), conf);
        } else {
            state = BinaryUploader.save(fileManager, request, conf);
        }
        return state;
    }
}
