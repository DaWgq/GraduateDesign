package com.dispatch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.dispatch.mapper")
public class LaborDispatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(LaborDispatchApplication.class, args);
    }
}
