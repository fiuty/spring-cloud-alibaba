package com.dayue.orderservice.service;

import com.dayue.orderservice.domain.Student;
import com.dayue.orderservice.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhengdayue
 * @time 2022/8/26 0:45
 */
@Service
@Slf4j
public class StudentServiceImpl implements StudentService{

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public void test1() throws InterruptedException {
        Student student = studentRepository.selectById(1L);
        log.info("version:{}", student.getVersion());
        student.setAge(19);
        Thread.sleep(30000L);
        studentRepository.updateById(student);
        log.info("修改后：{}",student.getVersion());
    }

    @Override
    public void test2() {
        Student student = studentRepository.selectById(1L);
        log.info("version:{}", student.getVersion());
        student.setAge(20);
        studentRepository.updateById(student);
    }
}
