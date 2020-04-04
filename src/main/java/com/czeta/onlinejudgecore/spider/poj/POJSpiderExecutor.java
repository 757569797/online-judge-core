package com.czeta.onlinejudgecore.spider.poj;

import com.czeta.onlinejudgecore.utils.spider.SpiderUtils;
import com.czeta.onlinejudgecore.utils.spider.contants.SpiderConstant;
import com.czeta.onlinejudgecore.utils.spider.request.SpiderRequest;
import com.czeta.onlinejudgecore.utils.spider.request.SpiderRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName POJSpiderExecutor
 * @Description
 * @Author chenlongjie
 * @Date 2020/4/4 16:15
 * @Version 1.0
 */
@Component
public class POJSpiderExecutor {
    @Autowired
    private POJResultSpider pojResultSpider;

    @Autowired
    private POJLoginSpider pojLoginSpider;

    /**
     * 定时登录任务，保持着登录状态
     */
    private void loginTask() {
        long startTime, endTime;
        System.out.println("开始登录");
        startTime = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id1", "pojspider1");
        map.put("password1", "123123");
        SpiderRequest spiderRequest = SpiderRequest.build("http://poj.org/login");
        spiderRequest.setMethod(SpiderConstant.Method.POST);
        spiderRequest.setSpiderRequestBody(SpiderRequestBody.form(map, "utf-8"));
        SpiderUtils.exec(spiderRequest, pojLoginSpider);
        endTime = System.currentTimeMillis();
        System.out.println("登录结束，耗时约" + ((endTime - startTime) + "ms"));
    }

    /**
     * 测试提交代码
     */
    public void submitCode(String problemId, String code, String language) {
        loginTask();
        long startTime, endTime;
        System.out.println("开始提交");
        startTime = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("problem_id", problemId);
        map.put("source", code);
        map.put("language", language);
        map.put("submit", "Submit");
        map.put("encoded", 1);
        SpiderRequest request = new SpiderRequest("http://poj.org/submit");
        request.setMethod(SpiderConstant.Method.POST);
        request.setSpiderRequestBody(SpiderRequestBody.form(map, "utf-8"));
        request.addCookie(POJLoginSpider.J_SESSION_ID, pojLoginSpider.sessionId);
        SpiderUtils.exec(request, pojResultSpider);
        endTime = System.currentTimeMillis();
        System.out.println("提交结束，耗时约" + ((endTime - startTime) + "ms"));
    }
}
