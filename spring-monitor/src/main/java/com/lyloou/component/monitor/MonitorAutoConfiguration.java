package com.lyloou.component.monitor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 监控的配置
 *
 * @author lilou
 * @since 2021/2/3
 */
@EnableAspectJAutoProxy
@Configuration
public class MonitorAutoConfiguration {
    @Value("${monitor.default-status:ON}")
    private String defaultStatus;

    /**
     * 多个包名用逗号隔开
     */
    @Value("${monitor.scan-packages:com.lyloou.component.monitor}")
    private String scanPackages;

    @Bean
    @ConditionalOnMissingBean(MonitorAspect.class)
    public MonitorAspect monitorAspect() {
        return new MonitorAspect();
    }

    // ConditionalOnMissingBean ？ 为什么要用这个注解？
    // 因为如果是在自己内部模块调用（如测试中）会重复。
    // 如果在外部调用，会不存在。
    @Bean
    @ConditionalOnMissingBean(ApplicationContextHelper.class)
    public ApplicationContextHelper applicationContextHelper() {
        return new ApplicationContextHelper();
    }

    @Bean
    @ConditionalOnMissingBean(MonitorConfig.class)
    public MonitorConfig monitorStatusConfig() {
        final MonitorConfig config = new MonitorConfig();
        config.setDefaultStatus(defaultStatus);
        config.setScanPackages(scanPackages);
        return config;
    }

    @Bean
    @ConditionalOnMissingBean(MonitorHandler.class)
    public MonitorHandler monitorHandler() {
        return new MonitorHandler();
    }

    @Bean
    @ConditionalOnMissingBean(MonitorController.class)
    public MonitorController monitorController() {
        return new MonitorController();
    }

    @Bean
    @ConditionalOnMissingBean(LogLevelController.class)
    public LogLevelController logLevelController() {
        return new LogLevelController();
    }
}
