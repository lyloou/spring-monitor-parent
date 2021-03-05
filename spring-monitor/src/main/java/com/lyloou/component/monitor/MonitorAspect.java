package com.lyloou.component.monitor;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;

/**
 * 监控的处理类
 *
 * @author lilou
 * @since 2021/2/3
 */
@Slf4j
@Aspect
public class MonitorAspect {
    @Autowired
    protected MonitorHandler handler;

    @Pointcut("@annotation(com.lyloou.component.monitor.MonitorMethod)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        final String key = getKey(pjp);
        if (isOffStatus(key)) {
            log.info("监控拦截，方法名：{}，当前时间：{}", key, new Date());
            return null;
        }
        return pjp.proceed();

    }

    protected boolean isOffStatus(String key) {
        final String status = handler.get(key);
        return Objects.equals(MonitorStatus.OFF.name(), status);
    }

    private String getKey(ProceedingJoinPoint pjp) throws NoSuchMethodException {

        // 获取类上的名称（如果有自定义的，就用自定义的，如果没有用类的全限定名）
        Class<?> classTarget = pjp.getTarget().getClass();
        final MonitorClass monitorClass = classTarget.getDeclaredAnnotation(MonitorClass.class);
        final String monitorClassValue = monitorClass.value();
        String className = Strings.isEmpty(monitorClassValue) ? classTarget.getName() : monitorClassValue;

        Class<?>[] par = ((MethodSignature) pjp.getSignature()).getParameterTypes();
        Method declaredMethod = classTarget.getMethod(pjp.getSignature().getName(), par);

        // 获取方法上的名称（如果有自定义的，就用自定义的，如果没有用方法的名称）
        final MonitorMethod monitorMethod = declaredMethod.getDeclaredAnnotation(MonitorMethod.class);
        final String monitorMethodValue = monitorMethod.value();
        String methodName = Strings.isEmpty(monitorMethodValue) ? declaredMethod.getName() : monitorMethodValue;

        return String.format("%s.%s", className, methodName);
    }
}
