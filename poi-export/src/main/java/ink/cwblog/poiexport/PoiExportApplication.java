package ink.cwblog.poiexport;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("ink.cwblog.poiexport.dao")
@SpringBootApplication
public class PoiExportApplication {

    public static void main(String[] args) {
        SpringApplication.run(PoiExportApplication.class, args);
    }

}
