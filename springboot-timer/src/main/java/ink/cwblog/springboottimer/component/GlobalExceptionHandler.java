package ink.cwblog.springboottimer.component;

import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常捕获
 *
 * @author chenw
 * @date 2021/4/1 16:22
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Exception 异常捕获
     *
     * @param e
     * @param <T>
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public <T> R<T> globalExceptionHandler(Exception e) {
        return R.failed("服务异常");
    }

}
