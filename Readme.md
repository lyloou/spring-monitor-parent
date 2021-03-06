# AOP 实现（定时器）方法监控——Monitor

## 描述

**背景**

1. 定时任务出现问题，在修复发布前，需要临时停下来，可之前又没有埋点；
2. 定时任务需要临时手动触发一下，可之前又没有埋点；

Monitor 就是用来解决上面这些问题的。

被 `@MonitorMethod` 标记的方法，并且方法所在的类被 `@MonitorClass` 标记，那么这个方法就可监控。

针对可监控的方法可以做下面这些事情：

1. 可以查看所有被监控方法的状态；(`/monitor/list`)
2. 可以对监控的方法启用或禁用；（`/monitor/put?key=com.lyloou.demo&value=OFF`）
3. 可以手动触发被监控的方法；（`/monitor/call?key=com.lyloou.demo.ScheduleService.runPer10s`）

**使用场景**

- 临时禁用定时器的运行；
- 临时禁用某个方法的运行；
- 临时手动调用定时方法；

## 原理：

1. 先将需要监控的方法，通过`注解+反射`加入到一个 map 中，；
2. 针对可能会监控的方法，通过注解标记（AOP 面向切面编程），所有对此方法的调用都会进入到`@Around`标记的环绕方法中；
3. 可以在`@Around`方法中拦截处理，判断方法的可用状态，是否需要向下进行；

## 使用：

**配置**

配置方式 1：

```yaml
monitor:
  default-status: "ON"
  scan-package: com.lyloou.demo
```

配置方式 2：

```java
@Configuration
public class MonitorConfiguration {
    @Bean
    public MonitorConfig monitorStatusConfig() {
        final MonitorConfig config = new MonitorConfig();
        config.setDefaultStatus(MonitorStatus.ON.name());
        // 多个包名，用逗号隔开
        config.setScanPackages(Joiner.on(",").join("com.lyloou.demo", "com.lyloou.another.demo"));
        return config;
    }
}
```

**代码**

```java
@EnableScheduling
@Service
@Slf4j
// 1. 在需要干预的类上加上 @MonitorClass
@MonitorClass
public class ScheduleService {
    // 2. 在需要干预的方法上加上 @MonitorMethod
    @MonitorMethod
    @Scheduled(cron = "0/10 * * * * *")
    public void runPer10s() {
        // 当被标记有@MonitorMethod的方法被调用时，会进入 MonitorAspect 中，判断方法的MonitorStatus是否为ON，如果是（默认是ON，可以通过api修改），才会向下进行
        log.info("runPer10s...");
    }
}
```

也可以在 @MonitorClass 和 @MonitorMethod 注解参数中加入自定义的名字做为 key。
默认的 key 为：`包名.类名.方法名`

![Readme-2021-03-05-14-51-28](http://cdn.lyloou.com/img/Readme-2021-03-05-14-51-28.png)

## api：

```shell script
# 列出被监控的方法
http://localhost:8080/monitor/list

# 获取某个具体的方法
http://localhost:8080/monitor/get?key=com.lyloou.monitor.ScheduleService.runPer10s

# 修改某个具体的方法
http://localhost:8080/monitor/put?key=com.lyloou.monitor.ScheduleService.runPer10s&value=OFF

# 修改所有被监听的方法
http://localhost:8080/monitor/putAll?value=OFF

# 手动调用被监听的方法
http://localhost:8080/monitor/call?key=com.lyloou.monitor.ScheduleService.runPer10s
```

具体逻辑查看 `MonitorController` 

## 源码
https://github.com/lyloou/spring-monitor-parent

## 扩展

- 也可以将 `@Scheduled` 做为一个切入点；
  (如 module `spring-monitor-schedule`，测试module：`spring-monitor-schedule-demo`)
