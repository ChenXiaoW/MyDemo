package ink.cwblog.springboottimer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.cwblog.springboottimer.pojo.Task;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 动态定时任务表 Mapper 接口
 * </p>
 *
 * @author chenw
 * @since 2021-08-16
 */
@Mapper
public interface TaskMapper extends BaseMapper<Task> {

}
