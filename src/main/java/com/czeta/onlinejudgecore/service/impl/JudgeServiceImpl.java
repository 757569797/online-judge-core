package com.czeta.onlinejudgecore.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.czeta.onlinejudge.dao.entity.JudgeType;
import com.czeta.onlinejudge.enums.JudgeServerStatus;
import com.czeta.onlinejudge.enums.JudgeTypeEnum;
import com.czeta.onlinejudge.utils.utils.DateUtils;
import com.czeta.onlinejudgecore.consts.JudgeMachineConst;
import com.czeta.onlinejudgecore.dao.mapper.JudgeTypeMapper;
import com.czeta.onlinejudgecore.machine.impl.QDOJMachineServiceImpl;
import com.czeta.onlinejudgecore.model.param.HeartbeatModel;
import com.czeta.onlinejudgecore.service.JudgeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @ClassName JudgeServiceImpl
 * @Description
 * @Author chenlongjie
 * @Date 2020/4/13 8:57
 * @Version 1.0
 */
@Slf4j
@Transactional
@Service
public class JudgeServiceImpl implements JudgeService {

    @Autowired
    private JudgeTypeMapper judgeTypeMapper;

    @Override
    public void updateOrSaveJudgeMachineByHeartbeat(HeartbeatModel heartbeatModel) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String judgeServerToken = request.getHeader(JudgeMachineConst.JUDGE_SERVER_TOKEN);
        JudgeType oldJudgeType = judgeTypeMapper.selectOne(Wrappers.<JudgeType>lambdaQuery()
                .eq(JudgeType::getHostname, heartbeatModel.getHostname()));
        JudgeType updatedJudgeType = new JudgeType();
        updatedJudgeType.setType(JudgeTypeEnum.JUDGE_MACHINE.getCode());
        // 该实例名称，与注解一致
        updatedJudgeType.setName(QDOJMachineServiceImpl.judgeMachineName);
        updatedJudgeType.setUrl(heartbeatModel.getService_url());
        updatedJudgeType.setStatus(JudgeServerStatus.NORMAL.getCode());
        updatedJudgeType.setHostname(heartbeatModel.getHostname());
        updatedJudgeType.setCpuCore(Short.valueOf(heartbeatModel.getCpu_core()));
        if (heartbeatModel.getRunning_task_number() == null) {
            updatedJudgeType.setTaskNumber((short) 1);
        } else {
            updatedJudgeType.setTaskNumber(Short.valueOf(heartbeatModel.getRunning_task_number()));
        }
        updatedJudgeType.setCpuUsage(heartbeatModel.getCpu());
        updatedJudgeType.setMemoryUsage(heartbeatModel.getMemory());
        updatedJudgeType.setVisitToken(judgeServerToken);
        updatedJudgeType.setJudgeVersion(heartbeatModel.getJudger_version());
        updatedJudgeType.setLastHeartBeat(DateUtils.getYYYYMMDDHHMMSS(new Date()));
        // 查询是否已经存在（是否是新的评测机实例心跳）
        if (oldJudgeType == null) {
            // 新的实例
            judgeTypeMapper.insert(updatedJudgeType);
        } else {
            // 更新旧实例心跳
            judgeTypeMapper.update(updatedJudgeType, Wrappers.<JudgeType>lambdaQuery()
                    .eq(JudgeType::getHostname, heartbeatModel.getHostname()));
        }
    }
}
