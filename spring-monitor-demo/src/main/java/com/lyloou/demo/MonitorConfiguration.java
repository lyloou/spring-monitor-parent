package com.lyloou.demo;

import com.google.common.base.Joiner;
import com.lyloou.component.monitor.MonitorConfig;
import com.lyloou.component.monitor.MonitorStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 监控的配置类
 *
 * @author lilou
 * @since 2021/2/3
 */
@Configuration
public class MonitorConfiguration {

    @Bean
    public MonitorConfig monitorStatusConfig() {
        final MonitorConfig config = new MonitorConfig();
        config.setDefaultStatus(MonitorStatus.ON.name());
        // config.setScanPackages(MonitorDemoApplication.class.getPackage().getName());
        config.setScanPackages(Joiner.on(",").join("com.lyloou.demo", "com.lyloou.another.demo"));
        return config;
    }

}
