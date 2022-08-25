package com.dayue.orderservice.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

/**
 * @author zhengdayue
 * @time 2022/3/24 18:43
 */
@Data
public class Student {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer age;

    @Version
    private Integer version;

}
