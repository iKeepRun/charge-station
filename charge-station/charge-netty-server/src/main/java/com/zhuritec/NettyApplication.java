package com.zhuritec;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableFeignClients(basePackages = "com.zhuritec.micro")
public class NettyApplication {
    public static void main(String[] args) {
       SpringApplication.run(NettyApplication.class, args);
        System.out.println("Springboot Application start success");
    }
}