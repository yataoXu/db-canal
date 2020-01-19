package com.evan;

import com.evan.annotation.EnableCanalClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = {"com.evan.mapper"})
@SpringBootApplication
@EnableCanalClient
public class CanalApplication {
	
	public static void main(String[] args){
        SpringApplication app = new SpringApplication(CanalApplication.class);
       // app.setWebEnvironment(false);
        app.run(args);
    }

}
