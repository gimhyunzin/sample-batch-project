package com.sample.batch.common.runner

import org.slf4j.LoggerFactory
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.boot.autoconfigure.batch.JobLauncherCommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class BatchJobRunner {
    private val logger = LoggerFactory.getLogger(BatchJobRunner::class.java)

    // this function must be bean!! Otherwise, run all Jobs(all Beans)
    // set your job name(with jvm option -DSpring.batch.job.name=$jobName)
    // then your job(with $jobName) auto run with JobLauncherCommandLineRunner
    @Bean
    fun createJobLauncherCommandLineRunner(jobLauncher: JobLauncher, jobExplorer: JobExplorer): JobLauncherCommandLineRunner? {
        val jobNames = System.getProperty("spring.batch.job.name")
        logger.info("Run Job Name: $jobNames")

        // spring job runner control
        // jobNames is null (no parameter) then this batch must be not run
        return when(jobNames.isNullOrBlank()) {
            true -> return null
            false -> {
                val runner = JobLauncherCommandLineRunner(jobLauncher, jobExplorer)
                runner.setJobNames(jobNames)
                runner
            }
        }
    }
}
