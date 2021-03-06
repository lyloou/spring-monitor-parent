package com.lyloou.component.monitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author lilou
 * @since 2021/3/4
 */
@EnableScheduling
@Service
@Slf4j
@MonitorClass
public class ScheduleService {

    // 10秒运行一次
    @MonitorMethod
    @Scheduled(cron = "0/10 * * * * *")
    public void runPer10s() {
        log.info("runPer10s...");
    }
}
