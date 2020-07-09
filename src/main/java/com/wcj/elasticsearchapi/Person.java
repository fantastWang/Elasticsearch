package com.wcj.elasticsearchapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangchaojie
 * @Description TODO
 * @Date 2020/7/8 14:56
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    private int _id;
    private int age;
    private String name;
}
