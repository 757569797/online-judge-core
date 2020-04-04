package com.czeta.onlinejudgecore.spider.poj;

import com.czeta.onlinejudgecore.utils.spider.processor.SpiderPageProcessor;
import com.czeta.onlinejudgecore.utils.spider.response.SpiderResponse;
import org.springframework.stereotype.Component;

/**
 * @ClassName TestHttpSpider
 * @Description
 * @Author chenlongjie
 * @Date 2020/4/4 14:55
 * @Version 1.0
 */
@Component
public class POJLoginSpider implements SpiderPageProcessor {
    public static final String SET_COOKIE = "Set-Cookie";
    public static final String J_SESSION_ID = "JSESSIONID";
    public volatile String sessionId;

    @Override
    public void process(SpiderResponse response) {
        String s = response.getHeaders().get(SET_COOKIE);
        this.sessionId = s.substring(s.indexOf("=") + 1, s.indexOf(";"));
    }
}
