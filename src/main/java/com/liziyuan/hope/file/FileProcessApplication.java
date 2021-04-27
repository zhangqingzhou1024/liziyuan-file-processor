package com.liziyuan.hope.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author zqz
 * @version 1.0
 * @date 2021-04-25 23:42
 */
@SpringBootApplication
@ComponentScan("com.liziyuan.hope.file")
public class FileProcessApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileProcessApplication.class, args);
        System.out.println(">>>>>>>>>>>>>启动成功>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

    }

}
