package com.czeta.onlinejudgecore.model.result;

import lombok.Data;

/**
 * @ClassName SubmitResultModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/22 10:38
 * @Version 1.0
 */
@Data
public class SubmitResultModel {
    private Long submitId;
    private Long problemId;
    private String time;
    private String memory;
    private String submitStatus;

    // 评测机评测的额外详情结果（针对每个评测样例的，格式为json格式：{ "msg":{...}, "code": }
    private String msgJson;

    // 用来修复爬虫评测方式下，目标OJ状态还是PENDING的状态
    private Boolean pending;
    private String spiderSubmitId;
}
