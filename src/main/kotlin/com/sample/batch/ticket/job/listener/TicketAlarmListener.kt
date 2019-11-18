package com.sample.batch.ticket.job.listener

import org.slf4j.LoggerFactory
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.listener.JobExecutionListenerSupport
import org.springframework.stereotype.Component


@Component
class TicketAlarmListener : JobExecutionListenerSupport() {
    private val logger = LoggerFactory.getLogger(TicketAlarmListener::class.java)

    override fun afterJob(jobExecution: JobExecution) {
        if (jobExecution.status === BatchStatus.COMPLETED) {
            logger.info("*************** Batch Status: Completed ***************")
        }

        if(jobExecution.status == BatchStatus.FAILED){
            logger.info("*************** Batch Status : Failed ***************")
        }
    }
}