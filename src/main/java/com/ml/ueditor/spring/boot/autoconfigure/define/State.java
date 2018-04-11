package com.ml.ueditor.spring.boot.autoconfigure.define;

/**
 * 处理状态接口
 *
 * @author hancong03@ml.com
 */
public interface State {

    boolean isSuccess();

    void putInfo(String name, String val);

    void putInfo(String name, long val);

    String toJSONString();

}
