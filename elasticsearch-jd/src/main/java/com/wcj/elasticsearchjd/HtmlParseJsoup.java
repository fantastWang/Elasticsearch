package com.wcj.elasticsearchjd;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangchaojie
 * @Description TODO
 * @Date 2020/7/8 16:39
 **/
@Component
public class HtmlParseJsoup {

    public List<JDPojo> parseJD(String keyword) throws IOException {
        List<JDPojo> list = new ArrayList<>();
        String url = "https://search.jd.com/Search?keyword=" + keyword;
        Document document = Jsoup.parse(new URL(url), 30000);
        Element element = document.getElementById("J_goodsList");
        Elements elements = element.getElementsByTag("li");
        for (Element el : elements) {
            JDPojo pojo = new JDPojo();
            String img = el.getElementsByTag("img").eq(0).attr("src");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String title = el.getElementsByClass("p-name").eq(0).text();
            pojo.setImg(img);
            pojo.setPrice(price);
            pojo.setTitle(title);
            list.add(pojo);
        }
        return list;
    }
}
