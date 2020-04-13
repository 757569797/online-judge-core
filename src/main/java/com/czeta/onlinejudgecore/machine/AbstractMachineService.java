package com.czeta.onlinejudgecore.machine;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.czeta.onlinejudge.dao.entity.JudgeType;
import com.czeta.onlinejudge.enums.BaseStatusMsg;
import com.czeta.onlinejudge.utils.enums.IBaseStatusMsg;
import com.czeta.onlinejudgecore.exception.MachineRuntimeException;
import com.czeta.onlinejudgecore.model.param.HeartbeatModel;
import com.czeta.onlinejudgecore.model.result.SubmitResultModel;
import com.czeta.onlinejudgecore.mq.SubmitMessage;

import java.util.List;

/**
 * @InterfaceName MachineServer
 * @Description 模板方法抽象类，用来提供需要转换的评测机访问方式（转换层）
 * @Author chenlongjie
 * @Date 2020/4/8 9:44
 * @Version 1.0
 */
public abstract class AbstractMachineService {
    private volatile static int index = 0;

    public SubmitResultModel execute(SubmitMessage submitMessage) throws Exception {
        // 根据评测机名称获取该评测机所部署的全部机器url，并通过负载均衡(轮询算法)获取合适的机器信息 todo 负载均衡选择评测机，加权轮询算法
        JudgeType targetJudgeMachine = null;
        synchronized (this) {
            List<JudgeType> judgeTypeList = submitMessage.getJudgeTypeList();
            if (CollectionUtils.isEmpty(judgeTypeList)) {
                throw new MachineRuntimeException(BaseStatusMsg.APIEnum.FAILED, "没有可用评测机");
            }
            if (judgeTypeList.size() >= index) {
                index = 0;
            }
            targetJudgeMachine = judgeTypeList.get(index);
            ++index;
        }
        submitMessage.setJudgeUrl(targetJudgeMachine.getUrl());
        // 模板方法：提供模板方法让具体评测机去实现，传入值和返回值均为抽象类所规定的，由子类自己去兼容
        return machineImplMethod(submitMessage);
    }

    abstract public SubmitResultModel machineImplMethod(SubmitMessage submitMessage) throws Exception;
}
