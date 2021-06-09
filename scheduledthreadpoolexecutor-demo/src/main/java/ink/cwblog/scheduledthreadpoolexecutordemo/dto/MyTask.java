package ink.cwblog.scheduledthreadpoolexecutordemo.dto;

import java.util.Date;

/**
 * @author chenw
 * @date 2021/6/1 15:01
 */
public class MyTask implements Runnable {

    private String name;

    public MyTask(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        try {
            System.out.println("Doing a task during : " + name + " - Time - " + new Date());
            Thread.sleep(1000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
