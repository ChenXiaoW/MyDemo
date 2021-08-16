package ink.cwblog.springboottimer.controller;

import com.baomidou.mybatisplus.extension.api.R;
import ink.cwblog.springboottimer.pojo.Task;
import ink.cwblog.springboottimer.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenw
 * @date 2021/8/16 16:26
 */
@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private ITaskService taskService;

    /**
     * 启用定时任务
     * @param taskNo
     */
    @GetMapping("/start/{taskNo}")
    public R startTask(@PathVariable("taskNo")String taskNo){
        taskService.start(taskNo);
        return R.ok(null);
    }

    /**
     * 暂停定时任务
     * @param taskNo
     */
    @GetMapping("/stop/{taskNo}")
    public R stopTask(@PathVariable("taskNo")String taskNo){
        taskService.stop(taskNo);
        return R.ok(null);
    }

    /**
     * 添加定时任务
     * @param task
     * @return
     */
    @PostMapping("")
    public R saveTask(@RequestBody Task task){
        return R.ok(taskService.saveTask(task));
    }

    /**
     * 查询定时任务列表
     * @return
     */
    @GetMapping("/list")
    public R getList(){
        return R.ok(taskService.getList());
    }

    /**
     * 更新任务
     * @param id
     * @param task
     * @return
     */
    @PutMapping("/{id}")
    public R updateTask(@PathVariable("id")Integer id,@RequestBody Task task){
        return R.ok(taskService.updateTask(id,task));
    }
}
