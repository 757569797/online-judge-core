package com.czeta.onlinejudgecore.task;

import com.czeta.onlinejudge.enums.JudgeTypeEnum;
import com.czeta.onlinejudgecore.machine.MachineClient;
import com.czeta.onlinejudgecore.mq.SubmitMessage;
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

    @Async
    public void task(SubmitMessage submitMessage) {
        log.info("current thread Id={}", Thread.currentThread().getId());
        if (submitMessage.getJudgeType().equals(JudgeTypeEnum.JUDGE_SPIDER.getCode())) {
            spiderClient.exec(submitMessage);
        } else if (submitMessage.getJudgeType().equals(JudgeTypeEnum.JUDGE_MACHINE.getCode())) {
            machineClient.exec(submitMessage);
        } else {
            // 容错统一处理
        }
    }

}
