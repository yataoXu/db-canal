package com.evan.config;

import com.evan.client.SimpleCanalClient;
import com.evan.properties.CanalProperties;
import com.evan.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;


@Slf4j
public class CanalClientConfiguration {


    /**
     * canal 配置
     */
    @Autowired
    private CanalProperties canalProperties;
    
    /**
     * 返回 bean 工具类
     *
     * @param
     * @return
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public BeanUtil beanUtil() {
        return new BeanUtil();
    }
    
    /**
     * 返回 canal 的客户端
     *
     */
    @Bean
    private SimpleCanalClient canalClient() {
        log.info("正在尝试连接 canal 客户端....");
        // 连接 canal 客户端
//        CanalClient canalClient = new SimpleCanalClient(canalConfig, MessageTransponders.defaultMessageTransponder());
        SimpleCanalClient canalClient = new SimpleCanalClient(canalProperties);
        log.info("正在尝试开启 canal 客户端....");
        // 开启 canal 客户端
        canalClient.start();
        log.info("启动 canal 客户端成功....");
        // 返回结果
        return canalClient;
    }
}
