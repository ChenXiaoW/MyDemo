package ink.cwblog.springboottimer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ink.cwblog.springboottimer.pojo.Task;

import java.util.List;

/**
 * <p>
 * 动态定时任务表 服务类
 * </p>
 *
 * @author chenw
 * @since 2021-08-16
 */
public interface ITaskService extends IService<Task> {

    /**
     * 执行定时任务
     * @param taskNo
     */
    void start(String taskNo);

    /**
     * 停止定时任务
     * @param taskNo
     */
    void stop(String taskNo);

    /**
     * 新增定时任务
     * @param task
     * @return
     */
    Task saveTask(Task task);

    /**
     * 查询任务列表
     * @return
     */
    List<Task> getList();

    /**
     * 更新任务
     * @param id
     * @param task
     * @return
     */
    Task updateTask(Integer id ,Task task);

}
