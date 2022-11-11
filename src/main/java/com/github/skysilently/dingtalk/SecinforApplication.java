package com.github.skysilently.dingtalk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan( { "com.github.skysilently.dingtalk.mapper"
} )
public class SecinforApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecinforApplication.class, args);
    }

}
