package ink.cwblog.canaldatasourcesdemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author chenw
 * @date 2021/6/8 17:17
 */
@Data
@ConfigurationProperties(prefix = "db")
public class DbSourceConfig {

    private List<Config> configs;

    @Data
    public static class Config{

        private String dbname;

        private String url;

        private String username;

        private String password;

    }
}
