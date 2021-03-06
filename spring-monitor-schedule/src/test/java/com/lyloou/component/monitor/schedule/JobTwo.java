package com.lyloou.component.monitor.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author lilou
 * @since 2021/3/6
 */
@Component
@Slf4j
public class JobTwo {
    @Scheduled(cron = "0/10 * * * * *")
    @Async(value = "jobExecutor")
    public void doSchedule1() {
        log.info("do schedule1 per 10s");
    }

    @Scheduled(cron = "0/15 * * * * *")
    @Async(value = "jobExecutor")
    public void doSchedule2() {
        log.info("do schedule2 per 15s");
    }
}
