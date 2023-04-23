package com.j3mall.openai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.j3mall.openai.mybatis.mapper")
@SpringBootApplication
public class OpenaiApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenaiApiApplication.class, args);
    }

}
