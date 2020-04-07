package com.czeta.onlinejudgecore.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * @ClassName ConsumerExceptionHandler
 * @Description 消费异常处理器
 * @Author chenlongjie
 * @Date 2020/3/31 19:46
 * @Version 1.0
 */
@Slf4j
public class ConsumerRuntimeExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
        log.error("Async任务异常处理方法:方法名={} 异常信息={}", method.getName(), ExceptionUtils.getStackTrace(throwable));
    }
}
