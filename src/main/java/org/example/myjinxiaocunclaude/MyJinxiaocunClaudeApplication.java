package org.example.myjinxiaocunclaude;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 开启定时任务支持 (库存预警)
public class MyJinxiaocunClaudeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyJinxiaocunClaudeApplication.class, args);
    }

}
