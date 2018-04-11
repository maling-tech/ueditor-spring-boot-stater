package com.ml.ueditor.spring.boot.autoconfigure;

import com.ml.ueditor.spring.boot.autoconfigure.define.ActionMap;
import com.ml.ueditor.spring.boot.autoconfigure.manager.DefaultFileManager;
import com.ml.ueditor.spring.boot.autoconfigure.manager.IUeditorFileManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(ActionMap.class)
@ConditionalOnWebApplication
public class UeditorAutoConfiguration {

    @ConditionalOnMissingBean(type = "com.ml.ueditor.spring.boot.autoconfigure.manager.IUeditorFileManager")
    @Bean
    IUeditorFileManager iUeditorFileManager() {
        return new DefaultFileManager();
    }

    @ConditionalOnMissingBean(type = "com.ml.ueditor.spring.boot.autoconfigure.UeditorManager")
    @Bean
    UeditorManager ueditorManager(IUeditorFileManager fileManager) {
        UeditorManager ueditorManager = new UeditorManager();
        ueditorManager.setFileManager(fileManager);
        return ueditorManager;
    }

    @ConditionalOnMissingBean(type = "com.ml.ueditor.spring.boot.autoconfigure.UeditorService")
    @Bean
    UeditorService ueditorService(UeditorManager ueditorManager) {
        return new UeditorService(ueditorManager);
    }
}
