package ink.cwblog.springboottimer.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ink.cwblog.springboottimer.dao.TaskMapper;
import ink.cwblog.springboottimer.pojo.Task;
import ink.cwblog.springboottimer.service.ITaskService;
import ink.cwblog.springboottimer.timer.TimerTaskHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 动态定时任务表 服务实现类
 * </p>
 *
 * @author chenw
 * @since 2021-08-16
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {

    @Autowired
    private TimerTaskHandle timerTaskHandle;

    /**
     * 执行定时任务
     *
     * @param taskNo
     */
    @Override
    public void start(String taskNo) {
        timerTaskHandle.start(taskNo);
        baseMapper.update(new Task().setTaskStatus(1), Wrappers.<Task>lambdaUpdate().eq(Task::getTaskNo,taskNo));
    }

    /**
     * 停止定时任务
     *
     * @param taskNo
     */
    @Override
    public void stop(String taskNo) {
        timerTaskHandle.stop(taskNo);
        baseMapper.update(new Task().setTaskStatus(0), Wrappers.<Task>lambdaUpdate().eq(Task::getTaskNo,taskNo));
    }

    /**
     * 新增定时任务
     *
     * @param task
     * @return
     */
    @Override
    public Task saveTask(Task task) {
        baseMapper.insert(task);
        return task;
    }

    /**
     * 查询任务列表
     *
     * @return
     */
    @Override
    public List<Task> getList() {
        return baseMapper.selectList(Wrappers.<Task>lambdaQuery().isNotNull(Task::getId));
    }

    /**
     * 更新任务
     *
     * @param id
     * @param task
     * @return
     */
    @Override
    public Task updateTask(Integer id, Task task) {
        baseMapper.update(task,Wrappers.<Task>lambdaUpdate().eq(Task::getId,id));
        Task task1 = baseMapper.selectById(id);
        timerTaskHandle.start(task1);
        return task1;
    }
}
