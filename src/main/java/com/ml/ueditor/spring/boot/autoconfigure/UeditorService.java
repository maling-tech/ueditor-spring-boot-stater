package com.ml.ueditor.spring.boot.autoconfigure;

import com.ml.ueditor.spring.boot.autoconfigure.define.ActionMap;
import com.ml.ueditor.spring.boot.autoconfigure.define.AppInfo;
import com.ml.ueditor.spring.boot.autoconfigure.define.BaseState;
import com.ml.ueditor.spring.boot.autoconfigure.define.State;
import com.ml.ueditor.spring.boot.autoconfigure.hunter.ImageHunter;
import com.ml.ueditor.spring.boot.autoconfigure.manager.IUeditorFileManager;
import com.ml.ueditor.spring.boot.autoconfigure.uploader.Uploader;
import com.ml.ueditor.spring.boot.autoconfigure.util.JsonUtils;

import javax.servlet.http.HttpServletRequest;

public class UeditorService {
    private UeditorManager ueditorManager;

    public UeditorService() {
    }

    public UeditorService(UeditorManager ueditorManager) {
        this.ueditorManager = ueditorManager;
    }

    public String exec(HttpServletRequest request) {
        String callbackName = request.getParameter("callback");
        String rootPath = request.getServletContext().getRealPath("/");
        return exec(request, callbackName, rootPath);
    }

    public String exec(HttpServletRequest request, String dataRootPath) {
        String callbackName = request.getParameter("callback");
        String rootPath = org.springframework.util.StringUtils.hasText(dataRootPath) ? dataRootPath : request.getServletContext().getRealPath("/");
        return exec(request, callbackName, rootPath);
    }

    private String exec(HttpServletRequest request, String callbackName, String rootPath) {
        if (callbackName != null) {
            if (!validCallbackName(callbackName)) {
                return new BaseState(false, AppInfo.ILLEGAL).toJSONString();
            }
            return callbackName + "(" + invoke(request, rootPath) + ");";
        } else {
            return invoke(request, rootPath);
        }
    }

    private String invoke(HttpServletRequest request, String rootPath) {
        String actionType = request.getParameter("action");

        if (actionType == null || !ActionMap.mapping.containsKey(actionType)) {
            return new BaseState(false, AppInfo.INVALID_ACTION).toJSONString();
        }
        if (ueditorManager == null || !ueditorManager.valid()) {
            return new BaseState(false, AppInfo.CONFIG_ERROR).toJSONString();
        }

        IUeditorFileManager fileManager = ueditorManager.getFileManager();

        State state = null;
        int actionCode = ActionMap.getType(actionType);
        ActionConfig conf;

        switch (actionCode) {

            case ActionMap.CONFIG:
                UeditorConfig allConfig = ueditorManager.getConfig();
                return JsonUtils.toJson(allConfig);

            case ActionMap.UPLOAD_IMAGE:
            case ActionMap.UPLOAD_SCRAWL:
            case ActionMap.UPLOAD_VIDEO:
            case ActionMap.UPLOAD_FILE:
                conf = ueditorManager.getConfig(actionCode, rootPath);
                state = new Uploader(request, conf).doExec(fileManager);
                break;

            case ActionMap.CATCH_IMAGE:
                conf = ueditorManager.getConfig(actionCode, rootPath);
                String[] list = request.getParameterValues(conf.getFieldName());
                state = new ImageHunter(fileManager, conf).capture(list);
                break;

            case ActionMap.LIST_IMAGE:
            case ActionMap.LIST_FILE:
                conf = ueditorManager.getConfig(actionCode, rootPath);
                int start = getStartIndex(request);
                state = fileManager.list(conf, start);
                break;
            default:
                break;
        }
        return state.toJSONString();
    }

    public int getStartIndex(HttpServletRequest request) {
        String start = request.getParameter("start");
        try {
            return Integer.parseInt(start);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * callback参数验证
     *
     * @param name 参数名
     * @return boolean 是否校验通过
     */
    public boolean validCallbackName(String name) {
        return name.matches("^[a-zA-Z_]+[\\w0-9_]*$");
    }

    public void setUeditorManager(UeditorManager ueditorManager) {
        this.ueditorManager = ueditorManager;
    }

    public UeditorManager getUeditorManager() {
        return ueditorManager;
    }
}
