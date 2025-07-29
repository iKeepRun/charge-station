package com.zhuritec;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class NettyApplication {
    public static void main(String[] args) {
       SpringApplication.run(NettyApplication.class, args);
        System.out.println("Springboot Application start success");
    }
}