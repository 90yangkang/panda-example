package com.example.shardingjdbc;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
@ComponentScan(basePackages = {"com.example.shardingjdbc"})
public class ShardingjdbcApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingjdbcApplication.class, args);
    }

}
