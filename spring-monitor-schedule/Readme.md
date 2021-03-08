# AOP 实现定时器方法监控——ScheduleMonitor

## 描述

**背景**

1. 定时任务出现问题，在修复发布前，需要临时停下来，可之前又没有埋点；
2. 定时任务需要临时手动触发一下，可之前又没有埋点；

ScheduleMonitor 就是用来解决上面这些问题的。

被 `@Scheduled` 标记的方法，可以被监控。

针对可监控的方法可以做下面这些事情：

1. 可以查看所有被监控方法的状态；
2. 可以对监控的方法启用或禁用；
3. 可以手动触发被监控的方法；

**使用场景**

- 临时禁用定时器的运行；
- 临时禁用某个方法的运行；
- 临时手动调用定时方法；

## 原理：

1. 在 spring bean 生命周期初始化阶段干预，实现 BeanPostProcessor 的 postProcessAfterInitialization 方法，
   在这个方法实现中，将标记有 `@Scheduled` 的方法缓存起来（用两个map来缓存：key->method、和 key->status；其中 key 由 `包名.类名.方法名` 拼接而成）。
2. 针对 `@Scheduled` 设置一个切点（AOP编程），当 `@Scheduled` 方法运行时，会进入到 `ScheduleMonitorAspect` 这个切面中，
   在环绕方法中（方法被`@Around`）对定时器方法过滤（通过第1步中的状态信息过滤）。
3. 通过 api 对第1步中的 map 状态加以修改。

## 使用：
引入pom依赖即可，由 spring-boot 自动装载。

```xml
<dependency>
    <groupId>com.lyloou</groupId>
    <artifactId>spring-monitor-schedule</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## api：

具体api查看 `ScheduleMonitorController`；

示例：
```
# 列出被监控的方法
http://localhost:8080/schedule/monitor/list

# 获取某个具体的方法
http://localhost:8080/schedule/monitor/get?key=com.lyloou.ScheduleService.runPer10s

# 修改某个具体的方法
http://localhost:8080/schedule/monitor/put?key=com.lyloou.ScheduleService.runPer10s&status=false

# 修改所有被监听的方法
http://localhost:8080/schedule/monitor/putAllStatus?status=false

# 手动调用被监听的方法
http://localhost:8080/schedule/monitor/call?key=com.lyloou.ScheduleService.runPer10s
```


