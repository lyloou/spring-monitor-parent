package com.lyloou.component.monitor;

import java.lang.annotation.*;

/**
 * 定义在监控方法上的注解
 *
 * @author lilou
 * @since 2021/2/2
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MonitorMethod {
    String value() default "";
}
