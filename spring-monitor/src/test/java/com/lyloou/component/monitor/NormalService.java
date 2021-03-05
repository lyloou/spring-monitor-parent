package com.lyloou.component.monitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lilou
 * @since 2021/3/4
 */
@Service
@Slf4j
@MonitorClass
public class NormalService {

    @MonitorMethod
    public void normal() {
        log.info("normal method called. ..");
    }
}
