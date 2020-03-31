package com.czeta.onlinejudgecore.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.czeta.onlinejudgecore.mq.SubmitMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    private static final int poolSize = 3;
    private ExecutorService executor;

    public SubmitConsumer() {
        executor = new ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(1000), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @KafkaListener(topics = {TOPIC_NAME})
    public void receive(List<ConsumerRecord<?, ?>> records) {
        log.info("record size={}", records.size());
        for (ConsumerRecord<?, ?> record : records) {
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            if (kafkaMessage.isPresent()) {
                SubmitMessage submitMessage = JSONObject.toJavaObject((JSONObject) JSONObject.parse((String) kafkaMessage.get()), SubmitMessage.class);
                log.info("message={}", JSONObject.toJSONString(submitMessage));
                executor.submit(new ConsumerRunnable(submitMessage));
            }
        }

    }
}
