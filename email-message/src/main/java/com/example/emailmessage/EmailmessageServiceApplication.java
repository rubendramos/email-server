package com.example.emailmessage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class EmailmessageServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailmessageServiceApplication.class, args);
	}

}
