package com.czeta.onlinejudgecore.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.czeta.onlinejudgecore.mq.SubmitMessage;
import com.czeta.onlinejudgecore.task.ConsumerAsyncTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @ClassName SubmitProducer
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/29 17:34
 * @Version 1.0
 */
@Component
@Slf4j
public class SubmitConsumer {
    private static final String TOPIC_NAME = "topic-submit";

    @Autowired
    private ConsumerAsyncTask consumerAsyncTask;

    @KafkaListener(topics = {TOPIC_NAME})
    public void receive(List<ConsumerRecord<?, ?>> records) {
        log.info("record size={}", records.size());
        for (ConsumerRecord<?, ?> record : records) {
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            if (kafkaMessage.isPresent()) {
                SubmitMessage submitMessage = JSONObject.toJavaObject((JSONObject) JSONObject.parse((String) kafkaMessage.get()), SubmitMessage.class);
                log.info("submitMessage={}", JSONObject.toJSONString(submitMessage));
                consumerAsyncTask.task(submitMessage);
            }
        }

    }
}
