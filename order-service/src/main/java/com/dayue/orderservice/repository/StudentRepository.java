package com.dayue.orderservice.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dayue.orderservice.domain.Student;

/**
 * @author zhengdayue
 * @time 2022/3/24 18:55
 */
public interface StudentRepository  extends BaseMapper<Student> {
    Student findByName(String name);
}
