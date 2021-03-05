package com.lyloou.component.monitor;

import java.lang.annotation.*;

/**
 * 定义在监控class上的注解
 *
 * @author lilou
 * @since 2021/2/2
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MonitorClass {
    String value() default "";
}
