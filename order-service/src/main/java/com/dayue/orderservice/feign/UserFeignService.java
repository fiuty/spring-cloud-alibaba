package com.dayue.orderservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author zhengdayue
 * @time 2022/7/28 19:58
 */
@FeignClient(value = "user-service")
public interface UserFeignService {

    @GetMapping("api/user/test")
    boolean test();
}
