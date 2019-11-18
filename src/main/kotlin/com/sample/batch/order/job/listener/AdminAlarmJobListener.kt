package com.sample.batch.order.job.listener

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.listener.JobExecutionListenerSupport
import org.springframework.stereotype.Component


@Component
class AdminAlarmJobListener : JobExecutionListenerSupport() {
    private val logger = LoggerFactory.getLogger(AdminAlarmJobListener::class.java)

    override fun afterJob(jobExecution: JobExecution) {
        logger.info("======== Job Status ===> ${jobExecution.status}")
        logger.info("======== Job Start ====> ${jobExecution.startTime}")
        logger.info("======== Job End ======> ${jobExecution.endTime}")
    }
}