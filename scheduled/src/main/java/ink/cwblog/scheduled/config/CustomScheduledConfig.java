package ink.cwblog.scheduled.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.util.StringUtils;

/**
 * @author chenw
 * @date 2021/7/26 18:16
 *
 * 使用配置文件配置异步线程开启关闭，替代@EnableScheduling的自启动
 */
@Configuration
public class CustomScheduledConfig {
    /**
     * 根据 CustomScheduledCondition.class 执行结果来判断是否执行该方法创建 ScheduledAnnotationBeanPostProcessor bean
     * @Conditional() 表示只有在所有指定条件都匹配时，组件才有资格注册。
     * @return
     */
    @Conditional(CustomScheduledCondition.class)
    @Bean
    public ScheduledAnnotationBeanPostProcessor processor() {
        return new ScheduledAnnotationBeanPostProcessor();
    }


    /**
     * 在 bean 注册之前去获取配置文件中的配置，如果有 Scheduled.enable 配置的话就根据配置执行，如果不存在配置的话就默认未为true;
     * Condition类：在 bean 定义即将注册之前立即检查条件，并且可以根据当时可以确定的任何标准自由否决注册。
     * @author chenw
     * @date 2021/7/26 18:23
     */
    static class CustomScheduledCondition implements Condition {
        @Override
        public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
            String property = conditionContext.getEnvironment().getProperty("Scheduled.enable");
            return StringUtils.hasText(property)?Boolean.valueOf(property):true;
        }
    }
}
