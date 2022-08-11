package com.example.cronservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

public class CronConfig  implements SchedulingConfigurer {
	@Value("${spring.scheduler.pool.size}")
	private Integer poolSize;

	private final String THREAD_PREFIX_NAME = "Cron-service-thread";

	@Override
	public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

		threadPoolTaskScheduler.setPoolSize(poolSize);
		threadPoolTaskScheduler.setThreadNamePrefix(THREAD_PREFIX_NAME);
		threadPoolTaskScheduler.initialize();

		scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
	}

}
