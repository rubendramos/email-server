package com.example.notification.jobscheduler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.notification.service.CronService;

@Component
public class JobScheduler {

	private static final Logger LOGGER = LoggerFactory.getLogger(JobScheduler.class);

	@Autowired
	private CronService cronService;

	//@Scheduled(cron = "0/30 * * * * ?") // run the job every 30 seconds 
	@Scheduled(cron = "0 10 * * * ?") // run the job every day at 10 am
	public void SpamJob() {
		final Set<String> emailsAddressToSpan = new HashSet<>(Arrays.asList("c@domain.com"));
		
		LOGGER.info("Starting Spam job... ");
		
		try {
			
			cronService.updateToSpan(emailsAddressToSpan);
			
		} catch (Exception e) {
			LOGGER.info("Spam job executed successfully!!! ");
		}

	}

}