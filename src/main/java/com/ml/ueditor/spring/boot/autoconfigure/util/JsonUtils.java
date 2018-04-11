package com.ml.ueditor.spring.boot.autoconfigure.util;

import org.springframework.util.ClassUtils;

import java.io.IOException;

/**
 * Json转换
 * 默认使用jackson
 * 再次fastJson
 * <p>
 * 参考Spring4中的base64工具类
 *
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * date 2015年5月13日下午4:58:33
 */
public class JsonUtils {

    /**
     * Json处理代理对象
     */
    private static final JsonDelegate delegate;

    private static final String ObjectMapperClass = "com.fasterxml.jackson.databind.ObjectMapper";
    private static final String JSONObjectClass = "com.alibaba.fastjson.JSONObject";

    static {
        JsonDelegate delegateToUse = null;
        // com.fasterxml.jackson.databind.ObjectMapper?
        if (ClassUtils.isPresent(ObjectMapperClass, JsonUtils.class.getClassLoader())) {
            delegateToUse = new JacksonDelegate();
        }
        // com.alibaba.fastjson.JSONObject?
        else if (ClassUtils.isPresent(JSONObjectClass, JsonUtils.class.getClassLoader())) {
            delegateToUse = new FastJsonDelegate();
        }
        delegate = delegateToUse;
    }

    /**
     * Json 委托，默认使用
     * 默认使用jackson
     * 再次fastJson
     * 最后使用jsonKit
     */
    private interface JsonDelegate {
        /**
         * 对象转json
         */
        String toJson(Object object);

        /**
         * json转对象
         */
        <T> T parse(String jsonString, Class<T> valueType);
    }

    /**
     * jackson委托
     */
    private static class JacksonDelegate implements JsonDelegate {
        private com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

        @Override
        public String toJson(Object object) {
            try {
                return objectMapper.writeValueAsString(object);
            } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public <T> T parse(String jsonString, Class<T> valueType) {
            try {
                return objectMapper.readValue(jsonString, valueType);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * fastJson委托
     */
    private static class FastJsonDelegate implements JsonDelegate {

        @Override
        public String toJson(Object object) {
            return com.alibaba.fastjson.JSONObject.toJSONString(object);
        }

        @Override
        public <T> T parse(String jsonString, Class<T> valueType) {
            return com.alibaba.fastjson.JSON.parseObject(jsonString, valueType);
        }
    }

    /**
     * 将 Object 转为json字符串
     *
     * @param object 对象
     * @return JsonString
     */
    public static String toJson(Object object) {
        if (delegate == null) {
            throw new RuntimeException("Jackson or Fastjson not supported");
        }
        return delegate.toJson(object);
    }

    /**
     * 将 json字符串 转为Object
     *
     * @param jsonString json字符串
     * @param valueType  类型
     * @return T 对象
     */
    public static <T> T parse(String jsonString, Class<T> valueType) {
        if (delegate == null) {
            throw new RuntimeException("Jackson or Fastjson not supported");
        }
        return delegate.parse(jsonString, valueType);
    }

}