package com.czeta.onlinejudgecore;

import com.alibaba.fastjson.JSONObject;
import com.czeta.onlinejudgecore.dao.mapper.UserMapper;
import com.czeta.onlinejudgecore.spider.TestSpider;
import com.czeta.onlinejudgecore.spider.poj.POJLoginSpider;
import com.czeta.onlinejudgecore.spider.poj.POJResultSpider;
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

    @Autowired
    private POJResultSpider pojResultSpider;

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
    void getMessage() throws Exception {
        SpiderRequest spiderRequest = SpiderRequest.build("https://www.nowcoder.com/practice/abc3fe2ce8e146608e868a70efebf62e?tpId=13&tqId=11154&tPage=1&rp=1&ru=/ta/coding-interviews&qru=/ta/coding-interviews/question-ranking");
        spiderRequest.setMethod(SpiderConstant.Method.GET);
        SpiderUtils.exec(spiderRequest, new TestSpider());
    }

    @Test
    void test() {
        long startTime, endTime;
        System.out.println("开始");
        startTime = System.currentTimeMillis();
        SpiderRequest spiderRequest = SpiderRequest.build("http://poj.org/status");
        spiderRequest.setMethod(SpiderConstant.Method.GET);
        SpiderUtils.exec(spiderRequest, pojResultSpider);
        endTime = System.currentTimeMillis();
        System.out.println("结束，耗时约" + ((endTime - startTime) + "ms"));
    }

}
