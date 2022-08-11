package com.example.emailconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class EmailConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailConfigApplication.class, args);
	}

}
