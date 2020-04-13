package com.czeta.onlinejudgecore.annotation;

import java.lang.annotation.*;

/**
 * @ClassName JudgeMachineName
 * @Description 评测机名称，用于扫描现有评测机实例并注册到可用评测机实例map中，供service使用
 * @Author chenlongjie
 * @Date 2020/4/8 9:43
 * @Version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JudgeMachineName {
    String name() default "";
}
