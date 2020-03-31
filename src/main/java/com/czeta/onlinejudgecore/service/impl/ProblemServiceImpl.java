package com.czeta.onlinejudgecore.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.czeta.onlinejudge.cache.model.CacheContestRankModel;
import com.czeta.onlinejudge.cache.model.RankItemModel;
import com.czeta.onlinejudge.dao.entity.*;
import com.czeta.onlinejudge.enums.BaseStatusMsg;
import com.czeta.onlinejudge.enums.SubmitStatus;
import com.czeta.onlinejudge.utils.utils.AssertUtils;
import com.czeta.onlinejudge.utils.utils.DateUtils;
import com.czeta.onlinejudgecore.cache.ContestRankRedisService;
import com.czeta.onlinejudgecore.dao.mapper.*;
import com.czeta.onlinejudgecore.model.result.SubmitResultModel;
import com.czeta.onlinejudgecore.mq.SubmitMessage;
import com.czeta.onlinejudgecore.service.ProblemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

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
    private ContestMapper contestMapper;

    @Autowired
    private SubmitMapper submitMapper;

    @Autowired
    private ContestRankRedisService contestRankRedisService;

    @Autowired
    private ContestRankMapper contestRankMapper;

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private SolvedProblemMapper solvedProblemMapper;

    @Autowired
    private UserMapper userMapper;


    @Override
    public void refreshDataOfProblemAfterJudge(SubmitMessage submitMessage, SubmitResultModel submitResultModel) {
        boolean ac = submitResultModel.getSubmitStatus().equals(SubmitStatus.ACCEPTED.getName());
        Long contestId = submitMessage.getSourceId();
        Long userId = submitMessage.getUserId();
        if (submitMessage.getSourceId() != 0) {
            // （1）该比赛的全局第一次提交，且redis中无数据：初始化该比赛实时榜单缓存，并持久化
            Contest contestInfo = contestMapper.selectById(contestId);
            Long currentTime = new Date().getTime() / 1000; // unix时间戳（second）
            Long startTime = DateUtils.getUnixTimeOfSecond(contestInfo.getStartTime());
            Long endTime = DateUtils.getUnixTimeOfSecond(contestInfo.getEndTime());
            if (currentTime >= startTime && currentTime <= endTime) {
                int count = submitMapper.selectCount(Wrappers.<Submit>lambdaQuery()
                        .eq(Submit::getSourceId, contestId));
                if (count <= 1 && !contestRankRedisService.exists(contestId)) {
                    // 初始化比赛榜单缓存
                    contestRankRedisService.initContestRankRedis(contestId, userId);
                    // 初始化后的比赛榜单也进行持久化
                    Map<Long, RankItemModel> mapModel = contestRankRedisService.getRankItemMapByContestIdFromCache(contestId);
                    CacheContestRankModel cacheContestRankModel = new CacheContestRankModel();
                    cacheContestRankModel.setContestId(contestId);
                    cacheContestRankModel.setRankItemMap(mapModel);
                    ContestRank contestRank = new ContestRank();
                    contestRank.setContestId(contestId);
                    contestRank.setRankJson(JSONObject.toJSONString(cacheContestRankModel));
                    contestRankMapper.insert(contestRank);
                }
            }
            // （2）非比赛的全局第一次提交，取出缓存并实时计算再存入，并持久化
            if (currentTime >= startTime && currentTime <= endTime) {
                // 更新缓存实时榜单
                contestRankRedisService.refreshContestRankRedis(contestId, submitMessage.getProblemId(), userId, ac);
                // 更新持久化实时榜单数据
                Map<Long, RankItemModel> mapModel = contestRankRedisService.getRankItemMapByContestIdFromCache(contestId);
                CacheContestRankModel cacheContestRankModel = new CacheContestRankModel();
                cacheContestRankModel.setContestId(contestId);
                cacheContestRankModel.setRankItemMap(mapModel);
                ContestRank contestRank = new ContestRank();
                contestRank.setRankJson(JSONObject.toJSONString(cacheContestRankModel));
                contestRankMapper.update(contestRank, Wrappers.<ContestRank>lambdaQuery().eq(ContestRank::getContestId, contestId));
            }
        }
        // 获取最终评测结果后进行更新相关的最终数据
        refreshSubmitProblem(submitResultModel, userId);
    }

    private void refreshSubmitProblem(SubmitResultModel submitResult, Long userId) {
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
