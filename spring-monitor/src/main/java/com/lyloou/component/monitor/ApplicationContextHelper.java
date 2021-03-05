package com.lyloou.component.monitor;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHelper implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
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
}
