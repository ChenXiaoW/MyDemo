# 自定义spring 异步任务的自动方式
使用配置文件中配置的方式来替代`@EnableScheduling`的自启动 
- 通过配置文件使异步任务灵活启动（在开发环境中不启动等）

- 注意：自定义以后不能在使用`@EnableScheduling`来启动