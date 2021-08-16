package ink.cwblog.myspringbootstarter.pojo;

import org.springframework.stereotype.Component;

/**
 * @author chenw
 * @date 2021/7/31 18:17
 */
public class Student {

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
}
