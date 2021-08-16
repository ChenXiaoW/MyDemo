package ink.cwblog.testmystarter.controller;

import ink.cwblog.myspringbootstarter.config.StudentConfigProperties;
import ink.cwblog.myspringbootstarter.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenw
 * @date 2021/7/31 18:21
 */
@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    private Student student;

    @RequestMapping("/getStudent")
    private Student getStudent() {
        return student;
    }

}
