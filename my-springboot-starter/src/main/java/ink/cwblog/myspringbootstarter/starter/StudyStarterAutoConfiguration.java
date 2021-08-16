package ink.cwblog.myspringbootstarter.starter;

import ink.cwblog.myspringbootstarter.config.StudentConfigProperties;
import ink.cwblog.myspringbootstarter.pojo.Student;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenw
 * @date 2021/7/31 17:53
 */
@Configuration
//导入自定义的配置类，供当前类使用
@EnableConfigurationProperties({StudentConfigProperties.class})
//当存在某个类的时候，此自动配置类才会生效，这里可以使用外部的类名
@ConditionalOnClass(Student.class)
// 只有web应用程序时此自动配置类才会生效
@ConditionalOnWebApplication
public class StudyStarterAutoConfiguration {

    /**
     * 当存在study.config.enable=true的配置时,这个Student bean才生效
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "study.config", name = "enable", havingValue = "true")
    public Student defaultStudent(StudentConfigProperties studyConfigProperties) {
        Student student = new Student();
        student.setAge(studyConfigProperties.getAge());
        student.setName(studyConfigProperties.getName());
        return student;
    }
}


