package com.sample.batch.ticket.step.mail

import com.sample.batch.common.model.Mail
import com.sample.batch.common.property.BatchJob
import com.sample.batch.ticket.model.Ticket
import com.sample.batch.ticket.step.mail.processor.MailAlarmProcessor
import com.sample.batch.ticket.step.mail.reader.MailAlarmReader
import com.sample.batch.ticket.step.mail.writer.MailAlarmWriter
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration


@Configuration
class MailAlarmStep {
    private val logger = LoggerFactory.getLogger(MailAlarmStep::class.java)

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    lateinit var mailAlarmReader: MailAlarmReader

    @Autowired
    lateinit var mailAlarmProcessor: MailAlarmProcessor

    @Autowired
    lateinit var mailAlarmWriter: MailAlarmWriter

    @Throws(Exception::class)
    fun createMailStep(): Step {
        logger.info("*************** Create Step : " + BatchJob.TICKET_MAIL_STEP_NAME.value + " ***************")

        return stepBuilderFactory.get(BatchJob.TICKET_MAIL_STEP_NAME.value)
                .chunk<MutableList<Ticket>, MutableList<Mail>>(100)
                .reader(mailAlarmReader)
                .processor(mailAlarmProcessor)
                .writer(mailAlarmWriter)
                .build()
    }
}