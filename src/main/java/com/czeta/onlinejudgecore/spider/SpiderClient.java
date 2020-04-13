package com.czeta.onlinejudgecore.spider;

import com.alibaba.fastjson.JSONObject;
import com.czeta.onlinejudge.enums.SubmitStatus;
import com.czeta.onlinejudgecore.annotation.SpiderNameAnnotationHandler;
import com.czeta.onlinejudgecore.exception.SpiderRuntimeException;
import com.czeta.onlinejudgecore.model.result.SubmitResultModel;
import com.czeta.onlinejudgecore.mq.SubmitMessage;
import com.czeta.onlinejudgecore.service.JudgeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName SpiderClient
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/30 15:06
 * @Version 1.0
 */
@Slf4j
@Component
public class SpiderClient {
    @Autowired
    private SpiderNameAnnotationHandler spiderHandler;

    @Autowired
    private JudgeService judgeService;

    public SubmitResultModel execute(SubmitMessage submitMessage) {
        SubmitResultModel submitResultModel = new SubmitResultModel();
        try {
            SpiderService spiderService = spiderHandler.getSpiderServiceMap().get(submitMessage.getJudgeName());
            submitResultModel = (SubmitResultModel) spiderService.execute(submitMessage);
        } catch (SpiderRuntimeException ex) {
            log.error("SpiderClient execute 爬虫运行异常");
            // 容错处理
            submitResultModel.setSubmitStatus(SubmitStatus.SYSTEM_ERROR.getName());
        } catch (Exception ex) {
            log.error("SpiderClient execute ExceptionMessage={} ExceptionStackTrace={}", ex.getMessage(), ExceptionUtils.getStackTrace(ex));
            // 容错处理
            submitResultModel.setSubmitStatus(SubmitStatus.SYSTEM_ERROR.getName());
        }
        // 更新爬虫评测方式表（心跳）
        judgeService.updateJudgeSpiderByHeartbeatCheck(submitMessage.getJudgeName());
        submitResultModel.setSubmitId(submitMessage.getSubmitId());
        submitResultModel.setProblemId(submitMessage.getProblemId());
        return submitResultModel;
    }
}
