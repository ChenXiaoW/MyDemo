package ink.cwblog.springboottimer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("ink.cwblog.springboottimer.dao")
@SpringBootApplication
public class SpringbootTimerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootTimerApplication.class, args);
    }

}
