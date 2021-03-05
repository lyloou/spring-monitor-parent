package com.lyloou.component.monitor;

import com.lyloou.component.monitor.model.Result;
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
@RequestMapping("/monitor")
public class MonitorController {

    @Autowired
    MonitorHandler handler;

    @RequestMapping("/list")
    public Result list() {
        return Result.success(handler.getMap());
    }


    @RequestMapping("/get")
    public Result get(String key) {
        final String data = handler.get(key);
        if (data == null) {
            return Result.error("key is invalid");
        } else {
            return Result.success(data);
        }
    }

    @RequestMapping("/put")
    public Result put(String key, String value) {
        if (handler.put(key, value)) {
            return Result.success("ok");
        } else {
            return Result.error("key or value is invalid");
        }
    }

    @RequestMapping("/putAll")
    public Result putAll(String value) {
        if (handler.putAll(value)) {
            return Result.success("ok");
        } else {
            return Result.error("value is invalid");
        }
    }

    @RequestMapping("/call")
    public Result call(String key) {
        if (handler.call(key)) {
            return Result.success("ok");
        } else {
            return Result.error("key in invalid");
        }
    }
}
