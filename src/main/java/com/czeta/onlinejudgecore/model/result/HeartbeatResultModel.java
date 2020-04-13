package com.czeta.onlinejudgecore.model.result;

import lombok.Data;

/**
 * @ClassName HeartbeatResultModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/4/9 10:47
 * @Version 1.0
 */
@Data
public class HeartbeatResultModel {
    private String data;
    private String error;

    public HeartbeatResultModel() {
        this.data = "success";
        this.error = null;
    }
}