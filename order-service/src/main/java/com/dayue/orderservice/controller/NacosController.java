package com.dayue.orderservice.controller;

import com.dayue.orderservice.feign.NacosFeign;
import com.dayue.orderservice.vo.nacos.NacosTokenVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengdayue
 * @time 2022/8/10 0:56
 */
@RestController
@RequestMapping("/api/nacos")
public class NacosController {

    @Autowired
    private NacosFeign nacosFeign;

    @GetMapping("/test")
    public NacosTokenVo test() {
        NacosTokenVo tokenInfo = nacosFeign.getTokenInfo("nacos", "nacos");
        return tokenInfo;
    }
}
