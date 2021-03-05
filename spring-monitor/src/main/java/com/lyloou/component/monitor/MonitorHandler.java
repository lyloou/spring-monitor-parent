package com.lyloou.component.monitor;


import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 监控处理类
 *
 * @author lilou
 * @since 2021/2/2
 */
@Service
@Slf4j
public class MonitorHandler {
    @Autowired
    MonitorConfig config;

    /**
     * 存放任务的状态的map
     */
    private final Map<String, MonitorStatus> map = new ConcurrentHashMap<>();
    private final Map<String, Method> keyMethodMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        final String scanPackage = config.getScanPackages();
        if (Strings.isEmpty(scanPackage)) {
            return;
        }
        final String[] scanPackageArr = scanPackage.split(",");
        Reflections reflections = new Reflections((Object[]) scanPackageArr);
        final MonitorStatus defaultStatus = getDefaultStatus();
        final Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(MonitorClass.class);
        for (Class<?> aClass : classSet) {

            // 获取类上的名称（如果有自定义的，就用自定义的，如果没有用类的全限定名）
            final MonitorClass monitorClass = aClass.getDeclaredAnnotation(MonitorClass.class);
            final String monitorClassValue = monitorClass.value();
            String className = Strings.isEmpty(monitorClassValue) ? aClass.getName() : monitorClassValue;

            final Method[] declaredMethods = aClass.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {

                // 获取方法上的名称（如果有自定义的，就用自定义的，如果没有用方法的名称）
                final MonitorMethod monitorMethod = declaredMethod.getDeclaredAnnotation(MonitorMethod.class);
                if (monitorMethod == null) {
                    continue;
                }
                final String monitorMethodValue = monitorMethod.value();
                String methodName = Strings.isEmpty(monitorMethodValue) ? declaredMethod.getName() : monitorMethodValue;

                // 将所有注释有MonitorMethod的方法都加入管理，默认为ON的状态
                final String name = String.format("%s.%s", className, methodName);
                map.put(name, defaultStatus);
                keyMethodMap.put(name, declaredMethod);
            }
        }

        log.info("Monitor scan package:{}, key:{}, default-status:{}", scanPackage, map.keySet(), defaultStatus);
    }

    /**
     * 获取默认状态
     *
     * @return 状态
     */
    private MonitorStatus getDefaultStatus() {
        final String status = config.getDefaultStatus();
        // 默认开
        if (Strings.isEmpty(status)) {
            return MonitorStatus.ON;
        }

        return MonitorStatus.valueOf(status);
    }

    public Map<String, MonitorStatus> getMap() {
        return map;
    }

    public String get(String key) {
        if (!map.containsKey(key)) {
            return null;
        }
        return map.get(key).name();
    }

    public boolean put(String key, String value) {
        if (!map.containsKey(key)) {
            return false;
        }
        if (!Arrays.stream(MonitorStatus.values()).map(Enum::name).collect(Collectors.toList()).contains(value)) {
            return false;
        }
        map.put(key, MonitorStatus.valueOf(value));
        return true;
    }

    public boolean putAll(String value) {
        if (!Arrays.stream(MonitorStatus.values()).map(Enum::name).collect(Collectors.toList()).contains(value)) {
            return false;
        }
        map.replaceAll((k, v) -> MonitorStatus.valueOf(value));
        return true;
    }

    // 手动调用方法
    public boolean call(String key) {
        if (!map.containsKey(key)) {
            return false;
        }
        final Method method = keyMethodMap.get(key);
        if (method == null) {
            return false;
        }

        final Object bean = ApplicationContextHelper.getBean(method.getDeclaringClass());
        //noinspection SSBasedInspection
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
