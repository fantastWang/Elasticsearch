package com.wcj.elasticsearchapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wangchaojie
 * @Description TODO
 * @Date 2020/7/8 14:37
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int age;
    private String name;
    private Date birth;
}
