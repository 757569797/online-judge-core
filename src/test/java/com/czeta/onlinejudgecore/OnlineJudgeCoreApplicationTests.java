package com.czeta.onlinejudgecore;

import com.alibaba.fastjson.JSONObject;
import com.czeta.onlinejudgecore.dao.mapper.UserMapper;
import com.czeta.onlinejudgecore.spider.poj.POJLoginSpider;
import com.czeta.onlinejudgecore.spider.poj.POJSpiderExecutor;
import com.czeta.onlinejudgecore.utils.spider.SpiderUtils;
import com.czeta.onlinejudgecore.utils.spider.contants.SpiderConstant;
import com.czeta.onlinejudgecore.utils.spider.request.SpiderRequest;
import com.czeta.onlinejudgecore.utils.spider.request.SpiderRequestBody;
import org.apache.shiro.codec.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class OnlineJudgeCoreApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private POJSpiderExecutor pojSpider;

    @Test
    void contextLoads() {
        System.out.println(JSONObject.toJSONString(userMapper.selectById(1)));
    }

    @Test
    void test1() throws Exception{
        String str = "aW1wb3J0IGphdmEudXRpbC4qOwpwdWJsaWMgY2xhc3MgTWFpbnsKCQoJcHVibGljIHN0YXRpYyB2b2lkIG1haW4oU3RyaW5nW10gYXJncykgewoJCQoJCVNjYW5uZXIgcmVhZD0gbmV3IFNjYW5uZXIoU3lzdGVtLmluKTsKCQkKCQl3aGlsZShyZWFkLmhhc05leHQoKSkKCQl7CgkJCWludCBhID0gcmVhZC5uZXh0SW50KCk7CgkJCWludCBiID0gcmVhZC5uZXh0SW50KCk7CgkJCVN5c3RlbS5vdXQucHJpbnRsbihhICsgYik7CgkJfQoJfQoJCn0=";
        pojSpider.submitCode("1000", str, "2");
    }

    @Test
    void test() {
        long startTime, endTime;
        System.out.println("开始登录");
        startTime = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id1", "pojspider1");
        map.put("password1", "123123");
        SpiderRequest spiderRequest = SpiderRequest.build("http://poj.org/login");
        spiderRequest.setMethod(SpiderConstant.Method.POST);
        spiderRequest.setSpiderRequestBody(SpiderRequestBody.form(map, "utf-8"));
        POJLoginSpider POJLoginSpider = new POJLoginSpider();
        SpiderUtils.exec(spiderRequest, POJLoginSpider);
        endTime = System.currentTimeMillis();
        System.out.println("登录结束，耗时约" + ((endTime - startTime) + "ms"));
    }

}
