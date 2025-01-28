package com.ehslab.batch.jobRunner;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class JobRunner {

    private final JobLauncher jobLauncher;
    private final ApplicationContext applicationContext;

    public JobRunner(JobLauncher jobLauncher, ApplicationContext applicationContext) {
        this.jobLauncher = jobLauncher;
        this.applicationContext = applicationContext;
    }

    public void runJob(String jobName) {
        try {
            // Retrieve Job by Name
            Job job = applicationContext.getBean(jobName, Job.class);

            // Create Job Parameters
            JobParameters params = new JobParametersBuilder()
                    .addString("JobID", String.valueOf(System.currentTimeMillis()))
                    .toJobParameters();

            // Run Job
            jobLauncher.run(job, params);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to execute job: " + jobName);
        }
    }
}

