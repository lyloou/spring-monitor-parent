package com.lyloou.component.monitor.schedule;


import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监控处理类
 *
 * @author lilou
 * @since 2021/2/2
 */
@Service
@Slf4j
public class ScheduleMonitorHandler {

    /**
     * 存放任务的状态的map
     */
    private final Map<String, Boolean> keyStatusMap = new ConcurrentHashMap<>();
    private final Map<String, Method> keyMethodMap = new ConcurrentHashMap<>();


    public Map<String, Boolean> listKeyStatus() {
        return keyStatusMap;
    }

    public Boolean getStatus(String key) {
        return keyStatusMap.get(key);
    }

    public boolean isKeyExisted(String key) {
        if (Strings.isEmpty(key)) {
            return false;
        }
        return keyStatusMap.containsKey(key);
    }

    public void putAllStatus(Boolean status) {
        keyStatusMap.replaceAll((k, v) -> status);
    }

    public void putKeyStatus(String key, Boolean status) {
        keyStatusMap.put(key, status);
    }

    public void putKeyMethod(String key, Method method) {
        keyMethodMap.put(key, method);
    }


    // 手动调用方法
    public boolean call(String key) {
        if (!keyStatusMap.containsKey(key)) {
            return false;
        }
        final Method method = keyMethodMap.get(key);
        if (method == null) {
            return false;
        }

        final Object bean = ApplicationContextHelper.getBean(method.getDeclaringClass());
        try {
            final boolean originStatus = method.isAccessible();
            method.setAccessible(true);
            method.invoke(bean);
            method.setAccessible(originStatus);
            return true;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }
}
