package ink.cwblog.springboottimer.timer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chenw
 * @date 2021/8/16 16:21
 *
 * 定时任务1
 */
@Slf4j
public class TestRunner1 implements Runnable {

    private Integer id;

    public TestRunner1(){}

    public TestRunner1(Integer id){
        this.id = id;
    }
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        log.info("id:{}",id);
        log.info("[TestRunner1] => [执行线程] {} => [执行时间] {}",Thread.currentThread().getName(),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
