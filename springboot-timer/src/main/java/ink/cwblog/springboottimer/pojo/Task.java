package ink.cwblog.springboottimer.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author chenw
 * @date 2021/8/16 15:16
 * 动态定时任务表
 */
@Data
@Accessors(chain =true)
public class Task {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 任务编号
     */
    private String taskNo;

    /**
     * 定时任务名称
     */
    private String taskName;
    /**
     * 定时任务描述
     */
    private String taskDesc;
    /**
     * 定时任务Cron表达式
     */
    private String taskExp;
    /**
     * 定时任务状态，0停用 1启用
     */
    private Integer taskStatus;
    /**
     * 定时任务的Runnable任务类完整路径
     */
    private String taskClass;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建时间
     */
    private Date createTime;
}
