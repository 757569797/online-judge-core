package com.czeta.onlinejudgecore.machine;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.czeta.onlinejudge.cache.model.CacheContestRankModel;
import com.czeta.onlinejudge.cache.model.RankItemModel;
import com.czeta.onlinejudge.dao.entity.Contest;
import com.czeta.onlinejudge.dao.entity.ContestRank;
import com.czeta.onlinejudge.dao.entity.Submit;
import com.czeta.onlinejudge.enums.SubmitStatus;
import com.czeta.onlinejudge.model.param.SubmitResultModel;
import com.czeta.onlinejudge.utils.utils.DateUtils;
import com.czeta.onlinejudgecore.cache.ContestRankRedisService;
import com.czeta.onlinejudgecore.dao.mapper.ContestMapper;
import com.czeta.onlinejudgecore.dao.mapper.ContestRankMapper;
import com.czeta.onlinejudgecore.dao.mapper.SubmitMapper;
import com.czeta.onlinejudgecore.mq.SubmitMessage;
import com.czeta.onlinejudgecore.service.ProblemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * @ClassName MachineClient
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/30 15:05
 * @Version 1.0
 */
@Slf4j
@Transactional
@Component
public class MachineClient {

    @Autowired
    private ContestMapper contestMapper;

    @Autowired
    private SubmitMapper submitMapper;

    @Autowired
    private ContestRankRedisService contestRankRedisService;

    @Autowired
    private ContestRankMapper contestRankMapper;

    @Autowired
    private ProblemService problemService;


    public void exec(SubmitMessage submitMessage) {
        tempHandler(submitMessage);
    }

    private void tempHandler(SubmitMessage submitMessage) {
        // mock评测结果
        boolean ac = false;
        if ((int) (Math.random() * 2) == 1) {
            ac = true;
        }
        log.info("ac={}", ac);
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
        SubmitResultModel submitResultModel = new SubmitResultModel();
        submitResultModel.setProblemId(submitMessage.getProblemId());
        submitResultModel.setSubmitId(submitMessage.getSubmitId());
        submitResultModel.setSubmitStatus(ac ? SubmitStatus.ACCEPTED.getName() : SubmitStatus.WRONG_ANSWER.getName());
        submitResultModel.setMemory("2000kb");
        submitResultModel.setTime("2000ms");
        log.info("submitResult={}", JSONObject.toJSONString(submitResultModel));
        problemService.refreshSubmitProblem(submitResultModel, userId);
    }
}
