package ink.cwblog.myspringbootstarter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author chenw
 * @date 2021/7/31 18:14
 */
@ConfigurationProperties(prefix = "study.config")
public class StudentConfigProperties {

    private Integer age;

    private String name;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "StudentConfigProperties [age=" + age + ", name=" + name + "]";
    }
}