package com.czeta.onlinejudgecore.task;

import com.czeta.onlinejudge.enums.JudgeTypeEnum;
import com.czeta.onlinejudge.utils.enums.IBaseStatusMsg;
import com.czeta.onlinejudgecore.exception.ConsumerException;
import com.czeta.onlinejudgecore.machine.MachineClient;
import com.czeta.onlinejudgecore.model.result.SubmitResultModel;
import com.czeta.onlinejudgecore.mq.SubmitMessage;
import com.czeta.onlinejudgecore.service.ProblemService;
import com.czeta.onlinejudgecore.spider.SpiderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @ClassName ConsumerRunnable
 * @Description 消费任务：处理消息（爬虫or评测机 分发器）
 * @Author chenlongjie
 * @Date 2020/3/30 11:16
 * @Version 1.0
 */
@Slf4j
@Component
public class ConsumerAsyncTask {

    @Autowired
    private SpiderClient spiderClient;

    @Autowired
    private MachineClient machineClient;

    @Autowired
    private ProblemService problemService;

    @Async
    public void task(SubmitMessage submitMessage) {
        log.info("current thread Id={}", Thread.currentThread().getId());
        // 获取评测结果
        SubmitResultModel submitResultModel = new SubmitResultModel();
        if (submitMessage.getJudgeType().equals(JudgeTypeEnum.JUDGE_SPIDER.getCode())) {
            submitResultModel = spiderClient.execute(submitMessage);
        } else if (submitMessage.getJudgeType().equals(JudgeTypeEnum.JUDGE_MACHINE.getCode())) {
            submitResultModel = machineClient.execute(submitMessage);
        } else {
            throw new ConsumerException(IBaseStatusMsg.APIEnum.PARAM_ERROR, "评测类型不合法");
        }
        // 获取评测结果后更新最终相关数据
        problemService.refreshDataOfProblemAfterJudge(submitMessage, submitResultModel);
    }

}
