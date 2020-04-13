package com.czeta.onlinejudgecore.machine;

import com.czeta.onlinejudge.enums.SubmitStatus;
import com.czeta.onlinejudgecore.annotation.JudgeMachineNameAnnotationHandler;
import com.czeta.onlinejudgecore.exception.MachineRuntimeException;
import com.czeta.onlinejudgecore.model.result.SubmitResultModel;
import com.czeta.onlinejudgecore.mq.SubmitMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName MachineClient
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/30 15:05
 * @Version 1.0
 */
@Slf4j
@Component
public class MachineClient {
    @Autowired
    private JudgeMachineNameAnnotationHandler judgeMachineNameAnnotationHandler;


    public SubmitResultModel execute(SubmitMessage submitMessage) {
        SubmitResultModel submitResultModel = new SubmitResultModel();
        try {
            AbstractMachineService machineService = judgeMachineNameAnnotationHandler.getMachineServiceMap().get(submitMessage.getJudgeName());
            submitResultModel = machineService.execute(submitMessage);
        } catch (MachineRuntimeException ex) {
            log.error("MachineClient execute MachineExecuteError: ExceptionMessage={} ExceptionStackTrace={}", ex.getMessage(), ExceptionUtils.getStackTrace(ex));
            // 容错处理
            submitResultModel.setSubmitStatus(SubmitStatus.SYSTEM_ERROR.getName());
        } catch (Exception ex) {
            log.error("MachineClient execute ExceptionMessage={} ExceptionStackTrace={}", ex.getMessage(), ExceptionUtils.getStackTrace(ex));
            // 容错处理
            submitResultModel.setSubmitStatus(SubmitStatus.SYSTEM_ERROR.getName());
        }
        submitResultModel.setSubmitId(submitMessage.getSubmitId());
        submitResultModel.setProblemId(submitMessage.getProblemId());
        return submitResultModel;
    }
}

