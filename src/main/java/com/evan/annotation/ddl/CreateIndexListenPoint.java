package com.evan.annotation.ddl;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.evan.annotation.ListenPoint;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;


/**
 * 创建索引操作
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ListenPoint(eventType = CanalEntry.EventType.CINDEX)
public @interface CreateIndexListenPoint {

    /**
     * canal 指令
     *
     * @return
     */
    @AliasFor(annotation = ListenPoint.class)
    String destination() default "";


    /**
     * 数据库实例
     *
     * @return
     */
    @AliasFor(annotation = ListenPoint.class)
    String[] schema() default {};

    /**
     * 监听的表
     *
     * @return
     */
    @AliasFor(annotation = ListenPoint.class)
    String[] table() default {};
}
