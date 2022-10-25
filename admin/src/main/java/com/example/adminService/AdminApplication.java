package com.example.adminService;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 劳威锟
 * @version 1.0
 * @description: TODO
 * @date 2022/10/24 10:50 AM
 */
@SpringBootApplication
@ComponentScan("com.example")
@MapperScan("com.example.adminService.mapper")
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
