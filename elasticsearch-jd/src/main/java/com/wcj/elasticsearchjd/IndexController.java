package com.wcj.elasticsearchjd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author wangchaojie
 * @Description TODO
 * @Date 2020/7/8 13:57
 **/
@Controller
public class IndexController {

    @Autowired
    private HtmlParseJsoup htmlParseJsoup;

    @GetMapping("/index")
    public String index(){
        return "index";
    }
}
