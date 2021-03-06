package com.lyloou.component.monitor.schedule;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @ Description   :
 * @ Author        :  Frank Zhang
 * @ CreateDate    :  2020/11/09
 * @ Version       :  1.0
 */
@Configuration
@EnableAspectJAutoProxy
public class ScheduleMonitorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ScheduleMonitorAspect.class)
    public ScheduleMonitorAspect scheduleMonitorAspect() {
        return new ScheduleMonitorAspect();
    }

    @Bean
    @ConditionalOnMissingBean(ApplicationContextHelper.class)
    public ApplicationContextHelper applicationContextHelper() {
        return new ApplicationContextHelper();
    }

    @Bean
    @ConditionalOnMissingBean(ScheduleMonitorHandler.class)
    public ScheduleMonitorHandler scheduleMonitorHandler() {
        return new ScheduleMonitorHandler();
    }

    @Bean
    @ConditionalOnMissingBean(ScheduleMonitorController.class)
    public ScheduleMonitorController scheduleMonitorController() {
        return new ScheduleMonitorController();
    }

}
