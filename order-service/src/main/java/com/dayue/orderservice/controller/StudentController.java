package com.dayue.orderservice.controller;


import com.dayue.orderservice.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengdayue
 * @time 2022/8/25 23:51
 */
@RestController
@RequestMapping("/api/student")
@Slf4j
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/test1")
    @Transactional
    public void test1() throws InterruptedException {
        studentService.test1();
    }

    @GetMapping("/test2")
    @Transactional
    public void test2() {
        studentService.test2();
    }

    @GetMapping("/test3")
    public String test3() {
        return "test3";
    }
}
