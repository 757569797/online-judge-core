package com.czeta.onlinejudgecore.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.czeta.onlinejudge.dao.entity.Problem;
import com.czeta.onlinejudge.dao.entity.SolvedProblem;
import com.czeta.onlinejudge.dao.entity.Submit;
import com.czeta.onlinejudge.enums.BaseStatusMsg;
import com.czeta.onlinejudge.enums.SubmitStatus;
import com.czeta.onlinejudge.model.param.SubmitResultModel;
import com.czeta.onlinejudge.utils.utils.AssertUtils;
import com.czeta.onlinejudge.utils.utils.DateUtils;
import com.czeta.onlinejudgecore.dao.mapper.ProblemMapper;
import com.czeta.onlinejudgecore.dao.mapper.SolvedProblemMapper;
import com.czeta.onlinejudgecore.dao.mapper.SubmitMapper;
import com.czeta.onlinejudgecore.dao.mapper.UserMapper;
import com.czeta.onlinejudgecore.service.ProblemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @ClassName ProblemServiceImpl
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/30 17:02
 * @Version 1.0
 */
@Slf4j
@Transactional
@Service
public class ProblemServiceImpl implements ProblemService {
    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private SolvedProblemMapper solvedProblemMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SubmitMapper submitMapper;

    public void refreshSubmitProblem(SubmitResultModel submitResult, Long userId) {
        // 校验参数：
        Problem problemInfo = problemMapper.selectById(submitResult.getProblemId());
        AssertUtils.notNull(problemInfo, BaseStatusMsg.APIEnum.PARAM_ERROR, "题目不存在");
        AssertUtils.isTrue(SubmitStatus.isContain(submitResult.getSubmitStatus()), BaseStatusMsg.APIEnum.PARAM_ERROR, "评测结果不合法");
        // 用户表
        SolvedProblem solvedProblem = solvedProblemMapper.selectOne(Wrappers.<SolvedProblem>lambdaQuery()
                .eq(SolvedProblem::getUserId, userId)
                .eq(SolvedProblem::getProblemId, submitResult.getProblemId())
                .eq(SolvedProblem::getSubmitStatus, SubmitStatus.ACCEPTED.getName()));
        if (solvedProblem == null && SubmitStatus.ACCEPTED.getName().equals(submitResult.getSubmitStatus())) {
            userMapper.updateAcNumIncrementOne(userId);
        }
        // 用户解决问题表
        if (solvedProblem == null) {
            SolvedProblem newSolvedProblem = solvedProblemMapper.selectOne(Wrappers.<SolvedProblem>lambdaQuery()
                    .eq(SolvedProblem::getUserId, userId)
                    .eq(SolvedProblem::getProblemId, submitResult.getProblemId()));
            newSolvedProblem.setSubmitStatus(submitResult.getSubmitStatus());
            newSolvedProblem.setLmTs(DateUtils.getYYYYMMDDHHMMSS(new Date()));
            solvedProblemMapper.updateById(newSolvedProblem);
        }
        // 题目信息表
        if (SubmitStatus.ACCEPTED.getName().equals(submitResult.getSubmitStatus())) {
            problemMapper.updateAcCountIncrementOne(submitResult.getProblemId());
            if (solvedProblem == null) {
                problemMapper.updateAcNumIncrementOne(submitResult.getProblemId());
            }
        }
        // 提交评测表
        Submit submit = submitMapper.selectById(submitResult.getSubmitId());
        AssertUtils.notNull(submit, BaseStatusMsg.APIEnum.PARAM_ERROR, "提交评测信息不存在");
        submit.setTime(submitResult.getTime());
        submit.setMemory(submitResult.getMemory());
        submit.setSubmitStatus(submitResult.getSubmitStatus());
        submitMapper.updateById(submit);
    }
}
