package com.czeta.onlinejudgecore.service;

import com.czeta.onlinejudge.model.param.SubmitResultModel;

/**
 * @ClassName ProblemService
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/30 17:14
 * @Version 1.0
 */
public interface ProblemService {
    /**
     * 获取评测最终结果后，进行最后的更新数据
     * @param submitResult
     * @param userId
     */
    void refreshSubmitProblem(SubmitResultModel submitResult, Long userId);
}
