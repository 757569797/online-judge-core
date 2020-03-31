package com.czeta.onlinejudgecore.service;

import com.czeta.onlinejudgecore.model.result.SubmitResultModel;
import com.czeta.onlinejudgecore.mq.SubmitMessage;

/**
 * @ClassName ProblemService
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/30 17:14
 * @Version 1.0
 */
public interface ProblemService {

    /**
     * 在获得最终评测结果之后，更新题目相关数据
     * @param submitMessage
     */
    void refreshDataOfProblemAfterJudge(SubmitMessage submitMessage, SubmitResultModel submitResultModel);
}
