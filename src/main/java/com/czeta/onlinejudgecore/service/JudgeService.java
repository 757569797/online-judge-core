package com.czeta.onlinejudgecore.service;

import com.czeta.onlinejudgecore.model.param.HeartbeatModel;

/**
 * @InterfaceName JudgeService
 * @Description
 * @Author chenlongjie
 * @Date 2020/4/13 8:56
 * @Version 1.0
 */
public interface JudgeService {
    /**
     * 初始化或更新评测机记录，通过评测机主动上报的心跳
     * todo 评测机定时检测时间差从而上报异常
     * @param heartbeatModel
     */
    void initOrUpdateJudgeMachineByHeartbeat(HeartbeatModel heartbeatModel);

    /**
     * 定时任务，只在系统启动时执行一次：初始化爬虫记录，通过注解扫描
     */
    void initJudgeSpiderByAnnotationScanTask();

    /**
     * 更新爬虫记录，通过主动心跳检测
     * todo 爬虫定时检测时间差从而上报异常
     */
    void updateJudgeSpiderByHeartbeatCheck(String spiderName);
}
