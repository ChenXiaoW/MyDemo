package ink.cwblog.scheduledthreadpoolexecutordemo;

import ink.cwblog.scheduledthreadpoolexecutordemo.dto.MyTask;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class ScheduledthreadpoolexecutorDemoApplicationTests {

    @Test
    void contextLoads() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        MyTask task1 = new MyTask ("Demo Task 1");
        MyTask task2 = new MyTask ("Demo Task 2");

        System.out.println("The time is : " + new Date());

        executor.schedule(task1, 20 , TimeUnit.SECONDS);
        executor.schedule(task1, 20 , TimeUnit.SECONDS);
        executor.schedule(task1, 20 , TimeUnit.SECONDS);
        executor.schedule(task2, 50 , TimeUnit.SECONDS);

        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();

    }

}
