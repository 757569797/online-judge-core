package com.czeta.onlinejudgecore;

import com.alibaba.fastjson.JSONObject;
import com.czeta.onlinejudgecore.dao.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OnlineJudgeCoreApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        System.out.println(JSONObject.toJSONString(userMapper.selectById(1)));
    }

}
