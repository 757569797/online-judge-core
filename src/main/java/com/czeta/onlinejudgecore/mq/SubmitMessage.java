package com.czeta.onlinejudgecore.mq;

import lombok.Data;


/**
 * @ClassName SubmitMessage
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/29 17:36
 * @Version 1.0
 */
@Data
public class SubmitMessage {
    // 提交的评测ID
    Long submitId;

    // 用户提交信息
    private Long userId;
    private String code;
    private String language;

    // 提交的题目信息
    Long problemId;
    private Long sourceId;
    private Integer timeLimit;
    private Integer memoryLimit;

    // 提交的题目评测方式信息
    private Short judgeStatus;
    private Short judgeType;
    private String judgeName;
    private String judgeUrl;
    private Integer problemType;
    private Integer spj;
    private Long spiderProblemId;

}