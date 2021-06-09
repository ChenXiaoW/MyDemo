package ink.cwblog.canaldatasourcesdemo.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenw
 * @date 2021/6/8 16:58
 */
@Configuration
@EnableConfigurationProperties(DbSourceConfig.class)
public class DataSourceAutoConfiguration {

    private DbSourceConfig dbSourceConfig;

    private Map<String, DbSourceConfig.Config> map = new HashMap<>(5);

    @Autowired
    public DataSourceAutoConfiguration(DbSourceConfig dbSourceConfig) {
        this.dbSourceConfig = dbSourceConfig;
    }

    @Bean(name = "liveDataSource")
    public DataSource liveDataSource() {
        DbSourceConfig.Config config = map.get("live");
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(config.getUrl());
        druidDataSource.setUsername(config.getUsername());
        druidDataSource.setPassword(config.getPassword());
        druidDataSource.setDriverClassName(DatabaseDriver.MYSQL.getDriverClassName());
        return druidDataSource;
    }


    @Bean(name = "bannerDataSource")
    public DataSource bannerDataSource() {
        DbSourceConfig.Config config = map.get("banner");
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(config.getUrl());
        druidDataSource.setUsername(config.getUsername());
        druidDataSource.setPassword(config.getPassword());
        druidDataSource.setDriverClassName(DatabaseDriver.MYSQL.getDriverClassName());
        return druidDataSource;
    }


    @PostConstruct
    private void init() {
        dbSourceConfig.getConfigs().stream().forEach(item -> {
            map.put(item.getDbname(), item);
        });
    }


}
