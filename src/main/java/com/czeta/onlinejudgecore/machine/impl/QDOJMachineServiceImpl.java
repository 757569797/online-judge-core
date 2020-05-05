package com.czeta.onlinejudgecore.machine.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.czeta.onlinejudge.enums.SubmitStatus;
import com.czeta.onlinejudgecore.annotation.JudgeMachineName;
import com.czeta.onlinejudgecore.consts.JudgeMachineConst;
import com.czeta.onlinejudgecore.machine.AbstractMachineService;
import com.czeta.onlinejudgecore.model.result.SubmitResultModel;
import com.czeta.onlinejudgecore.mq.SubmitMessage;
import com.czeta.onlinejudgecore.utils.spider.request.SpiderRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.shiro.codec.Base64;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName qdojMachineServiceImpl
 * @Description
 * @Author chenlongjie
 * @Date 2020/4/11 18:39
 * @Version 1.0
 */
@Slf4j
@JudgeMachineName(name = "QDJudgeServer")
public class QDOJMachineServiceImpl extends AbstractMachineService {

    // 评测机名称
    public static final String judgeMachineName = "QDJudgeServer";

    // 评测机的结果响应码对应着本系统的评测结果状态
    private static final Map<Integer, String> resultCodeToStatusMap = new HashMap<Integer, String>() {{
        put(-1, SubmitStatus.WRONG_ANSWER.getName());
        put(0, SubmitStatus.ACCEPTED.getName());
        put(1, SubmitStatus.TIME_LIMIT_EXCEEDED.getName());
        put(2, SubmitStatus.TIME_LIMIT_EXCEEDED.getName());
        put(3, SubmitStatus.MEMORY_LIMIT_EXCEEDED.getName());
        put(4, SubmitStatus.RUNTIME_ERROR.getName());
        put(5, SubmitStatus.SYSTEM_ERROR.getName());
    }};

    @Override
    public SubmitResultModel machineImplMethod(SubmitMessage submitMessage) throws Exception {
        System.out.println("submitMessage=" +  JSONObject.toJSONString(submitMessage));
        SubmitResultModel submitResultModel = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        try {
            JSONObject paramJson = new JSONObject();
            // 通用参数
            String code = Base64.decodeToString(submitMessage.getCode());
            paramJson.put("src", code);
            paramJson.put("language_config", JudgeMachineConst.getLanguageConfigByName(submitMessage.getLanguage()));
            paramJson.put("test_case_id", String.valueOf(submitMessage.getProblemId()));
            paramJson.put("max_cpu_time", submitMessage.getTimeLimit()); // ms
            paramJson.put("max_memory", submitMessage.getMemoryLimit() * 1024 * 1024); // B
            paramJson.put("output", false);
            // spj参数
            if (submitMessage.getSpj() == 1) {
                String spjCode = Base64.decodeToString(submitMessage.getSpjCode());
                paramJson.put("spj_src", spjCode);
                paramJson.put("spj_version", submitMessage.getSpjVersion());
                paramJson.put("spj_config", JudgeMachineConst.getSpjConfigByName(submitMessage.getSpjLanguage()));
                paramJson.put("spj_compile_config", JudgeMachineConst.getSpjCompileConfigByName(submitMessage.getSpjLanguage()));
            }

            // SerializerFeature.WriteMapNullValue处理Java语言下的特例："seccomp_rule":null，因为JSONObject序列化为字符串会自动过滤掉value为null的kv。
            SpiderRequestBody spiderRequestBody = SpiderRequestBody.json(JSONObject.toJSONString(paramJson, SerializerFeature.WriteMapNullValue), "utf-8");
            ByteArrayEntity entity =  new ByteArrayEntity(spiderRequestBody.getBody(), spiderRequestBody.getContentType());
            HttpPost httpPost = new HttpPost(submitMessage.getJudgeUrl() + "/judge");
            httpPost.setEntity(entity);
            httpPost.addHeader("X-Judge-Server-Token", submitMessage.getVisitToken());
            response = httpClient.execute(httpPost);
            StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer, StandardCharsets.UTF_8.name());
            JSONObject responseJson = JSONObject.parseObject(writer.toString());
            // 获取结果分析并返回
            submitResultModel = new SubmitResultModel();
            JSONObject msgCode = new JSONObject();
            msgCode.put("code", submitMessage.getCode());
            if (responseJson.get("err") != null) { // 特例
                if (responseJson.get("err").toString().equals("CompileError")) { // 编译错误
                    submitResultModel.setSubmitStatus(SubmitStatus.COMPILE_ERROR.getName());
                } else { // 评测机客户端错误
                    submitResultModel.setSubmitStatus(SubmitStatus.SYSTEM_ERROR.getName());
                }
                msgCode.put("msg", responseJson.get("data").toString());
                submitResultModel.setMsgJson(msgCode.toJSONString());
                log.info("QDOJMachineServiceImpl machineImplMethod msgJson = {}", msgCode.toJSONString());
            } else { // 通用
                long maxTime = 0;
                long maxMemory = 0;
                int finalResult = 0;
                JSONArray msgArray = new JSONArray();
                JSONArray array = responseJson.getJSONArray("data");
                // 遍历所有评测样例详情列表
                for (int i = 0; i < array.size(); ++i) {
                    JSONObject oneCase = array.getJSONObject(i);
                    maxTime = Math.max(maxTime, (long) (int) oneCase.get("cpu_time")); // 最大时间(ms)
                    maxMemory = Math.max(maxMemory, (long) (int) oneCase.get("memory")); // 最大内存(B)
                    finalResult = finalResult == 0 ? (int) oneCase.get("result") : finalResult;
                    JSONObject msgItem = new JSONObject();
                    msgItem.put("case_id", oneCase.get("test_case"));
                    msgItem.put("status", resultCodeToStatusMap.get(oneCase.get("result")));
                    msgItem.put("memory", (long) (int) oneCase.get("memory") / 1024 + "K");
                    msgItem.put("cpu_time", (long) (int) oneCase.get("cpu_time") + "MS");
                    msgItem.put("real_time", (long) (int) oneCase.get("real_time") + "MS");
                    msgArray.add(msgItem);
                }
                submitResultModel.setSubmitStatus(resultCodeToStatusMap.get(finalResult));
                submitResultModel.setTime(maxTime + "MS");
                submitResultModel.setMemory(maxMemory / 1024 + "K");
                msgCode.put("msg", JSONObject.toJSONString(msgArray));
                submitResultModel.setMsgJson(msgCode.toJSONString());
            }
        } finally {
            if (httpClient != null) {
                httpClient.close();
            }
            if (response != null) {
                response.close();
            }
        }
        return submitResultModel;

//        // mock评测结果
//        boolean ac = false;
//        if ((int) (Math.random() * 2) == 1) {
//            ac = true;
//        }
//        SubmitResultModel submitResultModel = new SubmitResultModel();
//        submitResultModel.setProblemId(submitMessage.getProblemId());
//        submitResultModel.setSubmitId(submitMessage.getSubmitId());
//        submitResultModel.setSubmitStatus(ac ? SubmitStatus.ACCEPTED.getName() : SubmitStatus.WRONG_ANSWER.getName());
//        submitResultModel.setMemory("2000kb");
//        submitResultModel.setTime("2000ms");
//        submitResultModel.setMsgJson("msgJson");
//        log.info("ac={}", ac);
//        log.info("submitResult={}", JSONObject.toJSONString(submitResultModel));
//        return submitResultModel;
    }
}
