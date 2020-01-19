package com.evan.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 从配置文件获取 canal 配置，前缀是 canal.client
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConfigurationProperties(prefix = "canal.client")
public class CanalProperties {

    /**
     * 配置信息
     */
    private Map<String, CanalProperties.Instance> instances = new LinkedHashMap<>();

    /**
     * 返回实例
     * @param
     * @return
     */
    public Map<String, CanalProperties.Instance> getInstances() {
        return instances;
    }

    /**
     * 设置实例
     * @param instances
     * @return
     */
    public void setInstances(Map<String, CanalProperties.Instance> instances) {
        this.instances = instances;
    }

    /**
     * canal 配置类
     */
    @Data
    public static class Instance {

        /**
         * 是否是集群模式
         */
        private boolean clusterEnabled;


        /**
         * zookeeper 地址
         */
        private Set<String> zookeeperAddress = new LinkedHashSet<>();

        /**
         * canal 服务器地址，默认是本地的环回地址
         */
        private String host;

        /**
         * canal 服务设置的端口，默认 11111
         */
        private int port = 11111;

        /**
         * 集群 设置的用户名
         */
        private String userName = "root";

        /**
         * 集群 设置的密码
         */
        private String password = "Qwe123!@#";

        /**
         * 批量从 canal 服务器获取数据的最多数目
         */
        private int batchSize = 1000;

        /**
         * 是否有过滤规则
         */
        private String filter;

        /**
         * 当错误发生时，重试次数
         */
        private int retryCount = 5;

        /**
         * 信息捕获心跳时间
         */
        private long acquireInterval = 1000;

        public Instance() {}

    }
}