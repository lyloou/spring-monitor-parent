package com.lyloou.demo;

import com.lyloou.component.monitor.MonitorClass;
import com.lyloou.component.monitor.MonitorMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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

    public static void main(String[] args) {
        List<BigDecimal> list = Arrays.asList(BigDecimal.valueOf(1.0), BigDecimal.valueOf(2.0));

        final BigDecimal bigDecimal = BigDecimal.valueOf(1.0);
        AtomicReference<BigDecimal> sum = new AtomicReference<>(bigDecimal);
        final List<BigDecimal> decimalList = list.stream().map(v -> {
            sum.set(sum.get().add(v));
            return v;
        }).collect(Collectors.toList());
        System.out.println(sum.get());
        System.out.println(decimalList);
    }
}
