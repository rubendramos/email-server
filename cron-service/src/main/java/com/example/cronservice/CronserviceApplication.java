package com.example.cronservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CronserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CronserviceApplication.class, args);
	}

}
