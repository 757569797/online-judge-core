package com.czeta.onlinejudgecore.controller;

import com.alibaba.fastjson.JSONObject;
import com.czeta.onlinejudgecore.consts.JudgeMachineConst;
import com.czeta.onlinejudgecore.model.param.HeartbeatModel;
import com.czeta.onlinejudgecore.model.result.HeartbeatResultModel;
import com.czeta.onlinejudgecore.service.JudgeService;
import com.czeta.onlinejudgecore.utils.spider.request.SpiderRequestBody;
import com.czeta.onlinejudgecore.utils.spider.response.SpiderResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.shiro.codec.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
/**
 * @ClassName JudgeMachineController
 * @Description
 * @Author chenlongjie
 * @Date 2020/4/9 10:45
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class JudgeMachineController {

    @Autowired
    private JudgeService judgeService;

    @PostMapping("/reportHeartbeat")
    public HeartbeatResultModel reportHeartbeat(@RequestBody HeartbeatModel heartbeatModel) {
        judgeService.initOrUpdateJudgeMachineByHeartbeat(heartbeatModel);
        return new HeartbeatResultModel();
    }




//    @PostMapping("/testCompileSpj")
//    public void testCompileSpj(@RequestParam String code) {
////        code = code.replaceAll("\r\n", "\n");
//        code = Base64.decodeToString(code);
//        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//        CloseableHttpResponse response = null;
//        try {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("src", code);
//            // 临时compile应该是随机数
//            String md5 = Md5Crypt.md5Crypt(("C" + code).getBytes("utf-8"));
//            jsonObject.put("spj_version", md5);
//            JSONObject jsonObject1 = JudgeMachineConst.getSpjCompileConfigByName("C");
//            jsonObject.put("spj_compile_config", jsonObject1);
//
//            SpiderRequestBody spiderRequestBody = SpiderRequestBody.json(JSONObject.toJSONString(jsonObject), "utf-8");
//            ByteArrayEntity entity =  new ByteArrayEntity(spiderRequestBody.getBody(), spiderRequestBody.getContentType());
//            HttpPost httpPost = new HttpPost("http://121.36.27.155:8080/compile_spj");
//            httpPost.setEntity(entity);
//            httpPost.addHeader("X-Judge-Server-Token", "93cf7ef4deb5e55d16f8a0f7ed9e18065800c0153dbc33402bc4820dbe1238c3");
//            response = httpClient.execute(httpPost);
//            SpiderResponse spiderResponse = SpiderResponse.build(null, response);
//            System.out.println(JSONObject.toJSONString(spiderResponse.getJsonObject()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                // 释放资源
//                if (httpClient != null) {
//                    httpClient.close();
//                }
//                if (response != null) {
//                    response.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
