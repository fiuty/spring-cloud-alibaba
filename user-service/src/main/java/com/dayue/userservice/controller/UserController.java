package com.dayue.userservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengdayue
 * @time 2022/7/24 22:39
 */
@RestController
@RefreshScope
@RequestMapping("/api/user")
public class UserController {

    @Value("${test}")
    private boolean test;

    @GetMapping("/test")
    public boolean test() {
        return test;
    }

}
