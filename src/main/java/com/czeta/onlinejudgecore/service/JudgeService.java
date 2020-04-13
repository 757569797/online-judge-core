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
    void updateOrSaveJudgeMachineByHeartbeat(HeartbeatModel heartbeatModel);
}
