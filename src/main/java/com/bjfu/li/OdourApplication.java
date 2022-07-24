package com.bjfu.li;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;

@SpringBootApplication

@MapperScan("com.bjfu.li.odour.mapper")
public class OdourApplication {

    public static void main(String[] args) {
        SpringApplication.run(OdourApplication.class, args);
    }

}
