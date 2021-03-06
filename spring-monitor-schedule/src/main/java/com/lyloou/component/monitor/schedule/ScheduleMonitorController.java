package com.lyloou.component.monitor.schedule;

import com.lyloou.component.monitor.schedule.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>监控管理api</p>
 *
 * @author lyloou
 * @since 2021/1/12
 */
@RestController
@RequestMapping("/schedule/monitor")
public class ScheduleMonitorController {

    @Autowired
    ScheduleMonitorHandler handler;

    @RequestMapping("/list")
    public Result list() {
        return Result.success(handler.listKeyStatus());
    }


    @RequestMapping("/get")
    public Result get(String key) {
        if (!handler.isKeyExisted(key)) {
            return Result.error("key is invalid");
        }

        final Boolean data = handler.getStatus(key);
        return Result.success(data);
    }

    @RequestMapping("/put")
    public Result put(String key, Boolean status) {
        if (status == null || !handler.isKeyExisted(key)) {
            return Result.error("key or value is invalid");
        }

        handler.putKeyStatus(key, status);
        final String result = String.format("key为%s的定时器%s", key, getStatusMsg(status));
        return Result.success(result);
    }

    @RequestMapping("/putAllStatus")
    public Result putAll(Boolean status) {
        if (status == null) {
            return Result.error("status is invalid");
        }

        handler.putAllStatus(status);
        return Result.success(String.format("所有的定时器%s", getStatusMsg(status)));
    }

    private String getStatusMsg(Boolean status) {
        return status ? "已启用" : "已禁用";
    }

    @RequestMapping("/call")
    public Result call(String key) {
        if (!handler.isKeyExisted(key)) {
            return Result.error("key is invalid");
        }

        final boolean result = handler.call(key);
        return Result.success(String.format("调用%s的状态为：%s", key, result));
    }
}
