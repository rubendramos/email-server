package com.example.emailbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableHystrix
@EnableHystrixDashboard
public class EmailBoxServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailBoxServiceApplication.class, args);
	}

}
