package com.dayue.orderservice.controller;

import com.dayue.orderservice.feign.UserFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author zhengdayue
 * @time 2022/7/21 23:52
 */
@RestController
@RequestMapping("/api")
@RefreshScope
public class OrderController {

    @Autowired
    private DiscoveryClient discoveryClient;

    // 新版本用application无法加载nacos配置中心文件，只能用bootstrap.yaml
    @Value(value = "${useLocalCache}")
    private boolean useLocalCache;

    @Autowired
    private UserFeignService userFeignService;


    @GetMapping("/test")
    public boolean test() {
        List<ServiceInstance> instances = discoveryClient.getInstances("user-service");
        if (instances != null && instances.size() != 0) {
            ServiceInstance instance = instances.get(0);
            Map<String, String> metadata = instance.getMetadata();
        }
        boolean test = userFeignService.test();
        return useLocalCache;
    }
}
