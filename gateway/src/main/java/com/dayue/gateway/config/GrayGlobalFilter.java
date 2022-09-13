package com.dayue.gateway.config;

import com.dayue.gateway.constants.GrayConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

/**
 * 全局过滤器，处理请求头
 * @author zhengdayue
 * @time 2022/9/12 23:57
 */
@Configuration
public class GrayGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private GrayConfiguration grayConfiguration;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerWebExchange newExchange = exchange.mutate().request(exchange.getRequest().mutate().headers((headers) -> {
            // 用户id，手机号，通常是解析cookie或者token获取用户id，这里为了方便演示，通过header设置用户id
            List<String> userIds = headers.get(GrayConstants.USER_ID);
            List<String> grayUserIds = grayConfiguration.getUserIds();
            // 该用户是内部人员，同时灰度配置有值，说明在进行灰度验证，把灰度验证版本号v2加入
            if (userIds != null && userIds.size() != 0 && grayUserIds != null && grayUserIds.contains(userIds.get(0))) {
                headers.put(GrayConstants.VERSION_ID, Collections.singletonList(GrayConstants.NEW_INSTANCE_VERSION_ID));
            }
        }).build()).build();
        return chain.filter(newExchange);
    }

    @Override
    public int getOrder() {
        // 优先级最高
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
