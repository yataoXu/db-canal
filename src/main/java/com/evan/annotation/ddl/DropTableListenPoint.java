package com.evan.annotation.ddl;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.evan.annotation.ListenPoint;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 删除表的监听器
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ListenPoint(eventType = CanalEntry.EventType.ERASE)
public @interface DropTableListenPoint {

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
}
