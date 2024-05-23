
package com.bookstore.scheduler;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.SimpleDateFormat;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;


@Configuration
@EnableScheduling
public class StuScheduler {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	@Scheduled(cron = "0 59 12 * * ?")
	public void launchJob() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		Logger logger = LoggerFactory.getLogger(StuScheduler.class);

		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String today = dateFormat.format(now);

		if (today.equals("2024-05-23")) {
			JobExecution jobExecution = jobLauncher.run(job,
					new JobParametersBuilder().addDate("launchDate", now).toJobParameters());
			logger.info("job starting" +jobExecution.getStatus());
			
		} else {
			logger.debug(today);
		}
	}
}
