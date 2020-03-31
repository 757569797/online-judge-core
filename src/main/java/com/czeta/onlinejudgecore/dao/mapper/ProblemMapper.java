package com.czeta.onlinejudgecore.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czeta.onlinejudge.dao.entity.Problem;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @InterfaceName ProblemMapper
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/12 20:40
 * @Version 1.0
 */
@Repository
public interface ProblemMapper extends BaseMapper<Problem> {

    @Update("UPDATE problem SET ac_num = ac_num + 1 WHERE id = #{problemId}")
    int updateAcNumIncrementOne(Long problemId);

    @Update("UPDATE problem SET ac_count = ac_count + 1 WHERE id = #{problemId}")
    int updateAcCountIncrementOne(Long problemId);
}
