package com.evan.annotation;

import com.evan.config.CanalClientConfiguration;
import com.evan.properties.CanalProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启 Canal 客户端
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({CanalProperties.class, CanalClientConfiguration.class})
public @interface EnableCanalClient {
}