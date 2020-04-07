package com.czeta.onlinejudgecore.spider.poj;

import com.czeta.onlinejudgecore.annotation.SpiderName;
import com.czeta.onlinejudgecore.model.result.SubmitResultModel;
import com.czeta.onlinejudgecore.mq.SubmitMessage;
import com.czeta.onlinejudgecore.spider.SpiderService;
import com.czeta.onlinejudgecore.utils.spider.SpiderUtils;
import com.czeta.onlinejudgecore.utils.spider.contants.SpiderConstant;
import com.czeta.onlinejudgecore.utils.spider.request.SpiderRequest;
import com.czeta.onlinejudgecore.utils.spider.request.SpiderRequestBody;
import org.springframework.data.util.Pair;


import java.util.*;

/**
 * @ClassName POJSpiderExecutor
 * @Description
 * @Author chenlongjie
 * @Date 2020/4/4 16:15
 * @Version 1.0
 */
@SpiderName(name = "POJ")
public class POJSpiderExecutor implements SpiderService {
    private static List<Pair<String, String>> account = new ArrayList<Pair<String, String>>() {{
        add(Pair.of("pojspider1", "123123"));
        add(Pair.of("pojvegetableno1", "123123"));
        add(Pair.of("pojvegetableno2", "123123"));
    }};

    private static Map<String, String> languageMap = new HashMap<String, String>() {{
        put("C", "5");
        put("C++", "4");
        put("Java", "2");
    }};

    /**
     * 定时登录任务，保持着登录状态
     */
    private void loginTask() {
        long startTime, endTime;
        System.out.println("开始登录");
        startTime = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        // 随机选取账号
        int index = (int) (Math.random() * account.size());
        map.put("user_id1", account.get(index).getFirst());
        map.put("password1", account.get(index).getSecond());
        SpiderRequest spiderRequest = SpiderRequest.build("http://poj.org/login");
        spiderRequest.setMethod(SpiderConstant.Method.POST);
        spiderRequest.setSpiderRequestBody(SpiderRequestBody.form(map, "utf-8"));
        SpiderUtils.exec(spiderRequest, new POJLoginSpider());
        endTime = System.currentTimeMillis();
        System.out.println("登录结束，耗时约" + ((endTime - startTime) + "ms"));
    }

    @Override
    public Object getResult(Object obj) {
        String spiderSubmitId = (String) obj;
        // 爬取结果
        SpiderRequest request = new SpiderRequest("http://poj.org/status");
        request.setMethod(SpiderConstant.Method.GET);
        POJResultSpider spiderProcess = new POJResultSpider();
        spiderProcess.setSpiderSubmitId(spiderSubmitId);
        return SpiderUtils.exec(request, spiderProcess);
    }


    @Override
    public Object execute(Object obj) {
        loginTask();
        SubmitMessage submitMessage = (SubmitMessage) obj;
        Map<String, Object> formMap = new HashMap<String, Object>() {{
            put("problem_id", submitMessage.getSpiderProblemId());
            put("source", submitMessage.getCode());
            put("language", languageMap.get(submitMessage.getLanguage()));
            put("submit", "Submit");
            put("encoded", 1);
        }};
        SpiderRequest request = new SpiderRequest("http://poj.org/submit");
        request.setMethod(SpiderConstant.Method.POST);
        request.setSpiderRequestBody(SpiderRequestBody.form(formMap, "utf-8"));
        request.addCookie(POJLoginSpider.J_SESSION_ID, POJLoginSpider.sessionId);
        SubmitResultModel submitResultModel = (SubmitResultModel) SpiderUtils.exec(request, new POJResultSpider());
        // 修复目标OJ状态还是PENDING的状态
        while (submitResultModel.getPending()) {
            submitResultModel = (SubmitResultModel) this.getResult(submitResultModel.getSpiderSubmitId());
        }
        return submitResultModel;
    }
}