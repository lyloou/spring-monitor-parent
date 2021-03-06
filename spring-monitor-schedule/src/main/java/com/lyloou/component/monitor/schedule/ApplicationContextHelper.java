package com.lyloou.component.monitor.schedule;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * ApplicationContextHelper
 * PriorityOrdered:[问题记录，为什么自定义的注解加在类上，此类在Bean初始化完成之后的postProcessAfterInitialization方法中查不到头上的注解？_wu_xiaozhi_xiao的博客-CSDN博客](https://blog.csdn.net/wu_xiaozhi_xiao/article/details/114394810)
 *
 * @author lyloou
 * @date 2021-03-06 9:45
 */
@Component
public class ApplicationContextHelper implements ApplicationContextAware, BeanPostProcessor, PriorityOrdered {
    private static ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHelper.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> targetClz) {
        return applicationContext.getBean(targetClz);
    }

    public static Object getBean(String claz) {
        return ApplicationContextHelper.applicationContext.getBean(claz);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return ApplicationContextHelper.applicationContext.getBean(name, requiredType);
    }

    public static <T> T getBean(Class<T> requiredType, Object... params) {
        return ApplicationContextHelper.applicationContext.getBean(requiredType, params);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        final ScheduleMonitorHandler handler = applicationContext.getBean(ScheduleMonitorHandler.class);

        final Method[] declaredMethods = bean.getClass().getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            final Scheduled scheduled = declaredMethod.getAnnotation(Scheduled.class);
            if (scheduled == null) {
                continue;
            }

            String key = getKey(bean, declaredMethod);
            handler.putKeyMethod(key, declaredMethod);
            handler.putKeyStatus(key, true);
        }
        return bean;
    }

    private String getKey(Object bean, Method method) {
        return bean.getClass().getCanonicalName() + "." + method.getName();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
