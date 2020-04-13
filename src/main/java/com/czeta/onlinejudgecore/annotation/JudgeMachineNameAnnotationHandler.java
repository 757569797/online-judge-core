package com.czeta.onlinejudgecore.annotation;

import com.czeta.onlinejudgecore.machine.AbstractMachineService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName JudgeMachineNameAnnotationHandler
 * @Description 扫描现有评测机实例并注册到可用评测机实例map中，供service使用
 * @Author chenlongjie
 * @Date 2020/4/8 9:44
 * @Version 1.0
 */
@Slf4j
@Data
@Component
public class JudgeMachineNameAnnotationHandler {
    // 爬虫名称与爬虫实例的映射，实现横向扩展
    public final Map<String, AbstractMachineService> machineServiceMap = new HashMap<>();

    {
        Reflections reflections = new Reflections("com.czeta.onlinejudgecore.machine");
        Set<Class<?>> services =
                reflections.getTypesAnnotatedWith(JudgeMachineName.class);
        for (Class<?> serviceClass : services) {
            JudgeMachineName machineName = serviceClass.getAnnotation(JudgeMachineName.class);
            try {
                machineServiceMap.put(machineName.name(), (AbstractMachineService) serviceClass.newInstance());
            } catch (InstantiationException e) {
                log.error("SpiderNameAnnotationHandler InstantiationException Exception={} StackTrace={}", e.getMessage(), ExceptionUtils.getStackTrace(e));
            } catch (IllegalAccessException e) {
                log.error("SpiderNameAnnotationHandler IllegalAccessException Exception={} StackTrace={}", e.getMessage(), ExceptionUtils.getStackTrace(e)); }
        }
    }
}
