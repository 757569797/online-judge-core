package com.czeta.onlinejudgecore.model.param;

import lombok.Data;

/**
 * @ClassName HeartbeatModel
 * @Description
 * @Author chenlongjie
 * @Date 2020/4/9 10:47
 * @Version 1.0
 */
@Data
public class HeartbeatModel {
    private String judger_version;
    private String hostname;
    private String running_task_number;
    private String cpu_core;
    private String memory;
    private String action;
    private String cpu;
    private String service_url;
}
