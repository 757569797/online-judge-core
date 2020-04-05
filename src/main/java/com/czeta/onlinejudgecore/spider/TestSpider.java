package com.czeta.onlinejudgecore.spider;

import com.czeta.onlinejudgecore.utils.spider.processor.SpiderPageProcessor;
import com.czeta.onlinejudgecore.utils.spider.response.SpiderResponse;
import org.jsoup.nodes.Document;


/**
 * @ClassName TestSpider
 * @Description
 * @Author chenlongjie
 * @Date 2020/4/5 10:42
 * @Version 1.0
 */
public class TestSpider implements SpiderPageProcessor {
    @Override
    public void process(SpiderResponse response) {
        Document doc = response.getDocument();
        System.out.println(doc.select("div[class=\"nk-container\"]").first().text());
    }
}
