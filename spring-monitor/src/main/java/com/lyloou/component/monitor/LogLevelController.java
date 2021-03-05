package com.lyloou.component.monitor;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.lyloou.component.monitor.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * [SpringBoot不重启修改日志级别【Slf4jj动态日志级别】 - 云+社区 - 腾讯云](https://cloud.tencent.com/developer/article/1639914)
 *
 * @ IDE    ：IntelliJ IDEA.
 * @ Date   ：2019/11/6  19:40
 * @ Desc   ：动态修改系统日志等级。
 */
@Slf4j
@RestController
@RequestMapping("/log/level")
public class LogLevelController {
    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

    /**
     * 修改日志级别接口
     *
     * @param level       日志级别
     * @param packageName 包名
     * @return 返回结果
     * 栗子:
     * curl [baseUrl]/log/level/change?packageName=[包名]&level=info
     * curl [baseUrl]/log/level/change?packageName=root&level=error
     */
    @GetMapping("/change")
    public Result changeLevel(@RequestParam String level, @RequestParam String packageName) {
        return Result.success(setLogger(packageName, level));
    }

    /**
     * 获取包名的日志级别
     *
     * @param packageName 包名
     * @return 返回结果
     * 栗子:
     * curl [baseUrl]/log/level/get?package=[包名]
     */
    @GetMapping("/get")
    public Result get(String packageName) {
        Map<String, String> map = new HashMap<>();

        final String currentApplicationPackageName = ApplicationContextHelper.class.getPackage().getName();
        map.put(currentApplicationPackageName, getLogger(currentApplicationPackageName));
        if (Strings.isNotEmpty(packageName)) {
            map.put(packageName, getLogger(packageName));
        }

        return Result.success(map);
    }

    @GetMapping("/del")
    public Result del(@RequestParam String packageName) {
        final List<Logger> loggerList = loggerContext.getLoggerList();
        loggerList.removeIf(next -> Objects.equals(next.getName(), packageName));
        return Result.success();
    }

    @GetMapping("/list")
    public Result list() {
        final List<Logger> loggerList = loggerContext.getLoggerList();
        //全局日志等级 + 项目日志等级 + 具体包的日志等级
        Map<String, String> map = new HashMap<>();
        map.put("root", getLogger("root"));
        loggerList.stream()
                .filter(logger -> Objects.nonNull(logger.getName()) && Objects.nonNull(logger.getLevel()))
                .forEach(logger -> map.put(logger.getName(), logger.getLevel().levelStr));
        return Result.success(map);
    }


    /**
     * 获取指定包日志级别             封装[设置日志级别+封装返回值信息]
     *
     * @param packageName 包名
     * @return String               日志级别信息
     */
    private String getLogger(String packageName) {
        return getLevel(packageName);
    }

    /**
     * 设置指定包日志级别             封装[日志级别检测+设置日志级别+封装返回值信息]
     *
     * @param packageName 包名
     * @return String               日志级别信息
     */
    private String setLogger(String packageName, String level) {
        boolean isAllowed = isAllowed(level);
        if (isAllowed) {
            setLevel(packageName, level);
        }
        return isAllowed
                ? packageName + "日志等级更改为:" + level
                : packageName + "日志等级修改失败,可用值[ERROR,WARN,INFO,DEBUG,TRACE]";
    }

    /**
     * 获取制定包的日志级别
     *
     * @param packageName 包名
     * @return String               日志级别
     */
    private String getLevel(String packageName) {
        Logger logger = loggerContext.getLogger(packageName);
        return hasNull(logger, logger.getLevel())
                ? ""
                : logger.getLevel().toString();
    }

    private boolean hasNull(Object... objects) {
        if (Objects.nonNull(objects)) {
            for (Object element : objects) {
                if (null == element) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 设置制定包的日志级别
     *
     * @param packageName 包名
     * @param level       日志等级
     */
    private void setLevel(String packageName, String level) {
        loggerContext.getLogger(packageName).setLevel(Level.toLevel(level));
    }

    private final String[] levels = {"OFF", "FATAL", "ERROR", "WARN", "INFO", "DEBUG", "TRACE", "ALL"};

    /**
     * 判断是否是合法的日志级别
     *
     * @param level 日志等级
     * @return boolean
     */
    private boolean isAllowed(String level) {
        return Arrays.asList(levels).contains(level.toUpperCase());
    }

}