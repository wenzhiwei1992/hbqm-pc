package com.smartpc.chiyun.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.smartpc.chiyun.aspect.DateEnum;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoRepeat {
    //这边是默认秒级别的控制
    DateEnum value() default DateEnum.minutes;
}
