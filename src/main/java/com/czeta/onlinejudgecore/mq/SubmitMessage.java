package com.czeta.onlinejudgecore.mq;

import com.czeta.onlinejudge.dao.entity.JudgeType;
import lombok.Data;

import java.util.List;


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
    private String code; // base64decoded
    private String language;

    // 提交的题目信息
    Long problemId;
    private Long sourceId;
    private Integer timeLimit; // ms
    private Integer memoryLimit; // MB

    // 提交的题目评测方式信息judgeStatus
    private Short judgeType;
    private String judgeName;
    private String judgeUrl;
    private List<JudgeType> judgeTypeList; //同名的评测方式列表
    private String visitToken;
    private Integer spj;
    private String spjCode; // base64decoded
    private String spjLanguage;
    private String spjVersion;
    private Long spiderProblemId;
}