package com.ml.ueditor.spring.boot.autoconfigure;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({UeditorAutoConfiguration.class})
public @interface EnableUeditor {
}
