package com.sample.batch.ticket.job

import com.sample.batch.common.property.BatchJob
import com.sample.batch.ticket.job.listener.TicketAlarmListener
import com.sample.batch.ticket.step.mail.MailAlarmStep
import com.sample.batch.ticket.step.lms.LMSAlarmStep
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableBatchProcessing
class TicketAlarmJob {
    private val logger = LoggerFactory.getLogger(TicketAlarmJob::class.java)

    @Autowired
    lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    lateinit var lmsAlarmStep: LMSAlarmStep

    @Autowired
    lateinit var mailAlarmStep: MailAlarmStep

    @Bean
    @Throws(Exception::class)
    fun createJob(): Job {
        logger.info("*************** Create Job : " + BatchJob.TICKET_JOB_NAME.value + " ***************")

        return jobBuilderFactory.get(BatchJob.TICKET_JOB_NAME.value)
                .incrementer(RunIdIncrementer())
                .listener(TicketAlarmListener())
                .flow(lmsAlarmStep.createSMSStep())
                .next(mailAlarmStep.createMailStep())
                .end()
                .build()
    }
}