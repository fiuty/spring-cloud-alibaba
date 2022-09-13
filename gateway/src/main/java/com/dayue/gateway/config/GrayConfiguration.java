package com.dayue.gateway.config;

import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 用于灰度发布，指定某些内部用户访问新实例
 * @author zhengdayue
 * @time 2022/9/12 21:40
 */
@Configuration
@NacosConfigurationProperties(dataId = "gateway", prefix = "gray.config", autoRefreshed = true)
@RefreshScope
public class GrayConfiguration {

    private List<String> userIds;

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
