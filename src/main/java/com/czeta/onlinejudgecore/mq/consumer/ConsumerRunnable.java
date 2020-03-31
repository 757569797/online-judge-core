package com.czeta.onlinejudgecore.mq.consumer;

import com.czeta.onlinejudge.enums.JudgeTypeEnum;
import com.czeta.onlinejudgecore.machine.MachineClient;
import com.czeta.onlinejudgecore.mq.SubmitMessage;
import com.czeta.onlinejudgecore.spider.SpiderClient;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ConsumerRunnable
 * @Description 消费任务：处理消息（爬虫or评测机 分发器）
 * @Author chenlongjie
 * @Date 2020/3/30 11:16
 * @Version 1.0
 */
@Slf4j
public class ConsumerRunnable implements Runnable {
    private SubmitMessage submitMessage;

    public ConsumerRunnable(SubmitMessage submitMessage) {
        this.submitMessage = submitMessage;
    }

    @Override
    public void run() {
        log.info("current thread Id={}", Thread.currentThread().getId());
//        if (submitMessage.getJudgeType().equals(JudgeTypeEnum.JUDGE_SPIDER.getCode())) {
//            new SpiderClient().exec(submitMessage);
//        } else if (submitMessage.getJudgeType().equals(JudgeTypeEnum.JUDGE_MACHINE.getCode())) {
//            new MachineClient().exec(submitMessage);
//        } else {
//            // 容错统一处理
//        }
    }
}
