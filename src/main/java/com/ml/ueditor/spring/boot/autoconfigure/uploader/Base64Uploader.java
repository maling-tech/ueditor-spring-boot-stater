package com.ml.ueditor.spring.boot.autoconfigure.uploader;

import com.ml.ueditor.spring.boot.autoconfigure.ActionConfig;
import com.ml.ueditor.spring.boot.autoconfigure.define.AppInfo;
import com.ml.ueditor.spring.boot.autoconfigure.define.BaseState;
import com.ml.ueditor.spring.boot.autoconfigure.define.FileType;
import com.ml.ueditor.spring.boot.autoconfigure.define.State;
import com.ml.ueditor.spring.boot.autoconfigure.manager.IUeditorFileManager;
import com.ml.ueditor.spring.boot.autoconfigure.util.PathFormat;
import org.springframework.util.Base64Utils;


public final class Base64Uploader {

    public static State save(IUeditorFileManager fileManager, String content, ActionConfig conf) {
        byte[] data = decode(content);
        long maxSize = conf.getMaxSize();

        if (!validSize(data, maxSize)) {
            return new BaseState(false, AppInfo.MAX_SIZE);
        }

        String suffix = FileType.getSuffix("JPG");

        String savePath = PathFormat.parse(conf.getSavePath(), conf.getFilename());

        savePath = savePath + suffix;
        String rootPath = conf.getRootPath();

        State storageState = fileManager.saveFile(data, rootPath, savePath);

        if (storageState.isSuccess()) {
            storageState.putInfo("url", PathFormat.format(savePath));
            storageState.putInfo("type", suffix);
            storageState.putInfo("original", "");
        }
        return storageState;
    }

    private static byte[] decode(String content) {
        return Base64Utils.decodeFromString(content);
    }

    private static boolean validSize(byte[] data, long length) {
        return data.length <= length;
    }

}