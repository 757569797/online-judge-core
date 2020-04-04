package com.czeta.onlinejudgecore.machine;

import com.alibaba.fastjson.JSONObject;
import com.czeta.onlinejudge.enums.SubmitStatus;
import com.czeta.onlinejudgecore.model.result.SubmitResultModel;
import com.czeta.onlinejudgecore.mq.SubmitMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName MachineClient
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/30 15:05
 * @Version 1.0
 */
@Slf4j
@Component
public class MachineClient {


    public SubmitResultModel execute(SubmitMessage submitMessage) {
        // mock评测结果
        boolean ac = false;
        if ((int) (Math.random() * 2) == 1) {
            ac = true;
        }
        SubmitResultModel submitResultModel = new SubmitResultModel();
        submitResultModel.setProblemId(submitMessage.getProblemId());
        submitResultModel.setSubmitId(submitMessage.getSubmitId());
        submitResultModel.setSubmitStatus(ac ? SubmitStatus.ACCEPTED.getName() : SubmitStatus.WRONG_ANSWER.getName());
        submitResultModel.setMemory("2000kb");
        submitResultModel.setTime("2000ms");
        log.info("ac={}", ac);
        log.info("submitResult={}", JSONObject.toJSONString(submitResultModel));
        return submitResultModel;
    }
}
