package com.example.workmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories(basePackages = "com.example.workmanager.global.auth")
public class WorkmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkmanagerApplication.class, args);
	}

}