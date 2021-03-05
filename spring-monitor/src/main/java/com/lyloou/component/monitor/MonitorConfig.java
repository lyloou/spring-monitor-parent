package com.lyloou.component.monitor;

import lombok.Data;

/**
 * 监控状态配置类
 *
 * @author lilou
 * @since 2021/2/3
 */
@Data
public class MonitorConfig {
    /**
     * 默认状态
     */
    private String defaultStatus;

    /**
     * 要扫描的包名，多个包名用逗号隔开
     */
    private String scanPackages;
}
