package com.ehslab.batch;

import com.ehslab.batch.jobRunner.JobRunner;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BatchApplication {

	public static void main(String[] args) {
		//SpringApplication.run(BatchApplication.class, args);
		ConfigurableApplicationContext context = SpringApplication.run(BatchApplication.class, args);

		// Retrieve JobRunner
		JobRunner jobRunner = context.getBean(JobRunner.class);
		System.out.println(System.currentTimeMillis());
		// Run inventoryJob
		jobRunner.runJob("inventoryJob");
		System.out.println(System.currentTimeMillis());
	}
}
