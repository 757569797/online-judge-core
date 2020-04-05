package com.czeta.onlinejudgecore.spider.poj;

import com.czeta.onlinejudgecore.utils.spider.processor.SpiderPageProcessor;
import com.czeta.onlinejudgecore.utils.spider.response.SpiderResponse;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;



/**
 * @ClassName POJSpider
 * @Description
 * @Author chenlongjie
 * @Date 2020/4/4 16:01
 * @Version 1.0
 */
@Component
public class POJResultSpider implements SpiderPageProcessor {
    @Override
    public void process(SpiderResponse response) {
        Document doc = response.getDocument();
        Elements elements = doc.select("table.a>tbody tr[align=center]>td");
        String submitId = elements.get(0).text();
        String result = elements.get(3).text();
        String memory = elements.get(4).text();
        String time = elements.get(5).text();
        System.out.println(submitId);
        System.out.println(result);
        System.out.println(memory);
        System.out.println(time);
    }
}
