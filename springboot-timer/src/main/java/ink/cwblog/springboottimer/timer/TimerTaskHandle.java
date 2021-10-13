package ink.cwblog.springboottimer.timer;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import ink.cwblog.springboottimer.dao.TaskMapper;
import ink.cwblog.springboottimer.pojo.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author chenw
 * @date 2021/8/16 15:39
 */
@Slf4j
@Component
public class TimerTaskHandle {

    //正在运行的任务
    private static ConcurrentHashMap<String, ScheduledFuture> runTasks = new ConcurrentHashMap<>();

    // 线程池任务调度
    private ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

    @Autowired
    private TaskMapper taskMapper;


    /**
     * 初始化线程池任务调度
     */
    @PostConstruct
    public void init() {
        this.threadPoolTaskScheduler.setPoolSize(32);
        this.threadPoolTaskScheduler.setThreadNamePrefix("task-thread-");
        this.threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        this.threadPoolTaskScheduler.initialize();
        //初始化数据
        this.getAllTaskFromDb();
    }

    /**
     * 从数据库中获取所有定时任务
     */
    private void getAllTaskFromDb() {
        List<Task> tasks = taskMapper.selectList(Wrappers.<Task>lambdaQuery().eq(Task::getTaskStatus, 1));
        if (CollectionUtils.isEmpty(tasks)) {
            log.info("当前不存在可执行的定时任务");
            return;
        }
        tasks.forEach(this::start);
        log.info("当前定时任务数:{}", tasks.size());
    }

    /**
     * 根据定时任务编号，启动定时任务
     *
     * @param taskNo
     */
    public void start(String taskNo) {
        Assert.notNull(taskNo, "任务编号不能为空");
        Task task = taskMapper.selectOne(Wrappers.<Task>lambdaQuery().eq(Task::getTaskNo, taskNo));
        Assert.notNull(task, "当前任务不存在执行");
        Assert.isTrue(1 != task.getTaskStatus(), "当前任务已执行");
        this.start(task);
    }

    /**
     * 根据定时任务详细，启动定时任务
     *
     * @param task
     */
    public void start(Task task) {
        Assert.notNull(task, "任务不能为空");
        // 校验是否存在现有任务中，如果存在则先做移除，再做新增
        ScheduledFuture scheduledFuture = TimerTaskHandle.runTasks.get(task.getTaskNo());
        if (!ObjectUtils.isEmpty(scheduledFuture)) {
            this.stop(task.getTaskNo());
        }
        try {
            //获取并实例化Runnable任务类
            Class<?> clazz = Class.forName(task.getTaskClass());
            Runnable taskRunner = (Runnable) clazz.newInstance();

           /*
            带参
            Class[] classes = {Integer.class};
            Object[] objects = {1};
            Class<?> clazz = Class.forName(task.getTaskClass());
            Constructor<?> constructor = clazz.getConstructor(classes);
            Runnable  taskRunner =(Runnable )constructor.newInstance(objects);*/
            //构建cron触发器
            CronTrigger cronTrigger = new CronTrigger(task.getTaskExp());
            //组建任务调度器，并加入到执行任务列表中
            TimerTaskHandle.runTasks.put(task.getTaskNo(), Objects.requireNonNull(this.threadPoolTaskScheduler.schedule(taskRunner, cronTrigger)));
            log.info("当前任务[{}]已启动", task.getTaskNo());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 根据定时任务编号,停止定时任务
     *
     * @param taskNo
     */
    public void stop(String taskNo) {
        Assert.notNull(taskNo, "任务编号不能为空");
        //尝试中断任务
        TimerTaskHandle.runTasks.get(taskNo).cancel(true);
        TimerTaskHandle.runTasks.remove(taskNo);
        log.info("当前任务[{}]已停止", taskNo);
    }


}
