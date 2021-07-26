package ink.cwblog.scheduled.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author chenw
 * @date 2021/7/26 18:10
 *
 * 定时任务
 */
@Component
public class MyJob {


    @Scheduled(cron = "*/1 * * * * ? ")
    public void job(){
        System.out.println("定时任务执行");
    }
}
