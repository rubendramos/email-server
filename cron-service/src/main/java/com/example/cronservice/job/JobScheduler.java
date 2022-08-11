package com.example.cronservice.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.cronservice.service.SpanService;



@Component
public class JobScheduler {

	private static final Logger LOGGER = LoggerFactory.getLogger(JobScheduler.class);

	@Autowired
	private SpanService spanService;

	//@Scheduled(cron = "0 0/2 * * * ?")  // run the job every two minutes
	@Scheduled(cron = "0 0/1 * * * ?")  // run the job every one minutes
	public void SpanJob() {
		LOGGER.info("Span job starting ... ");
		spanService.updateSpan();
		LOGGER.info("Span job executed successfully ... ");

	}

}