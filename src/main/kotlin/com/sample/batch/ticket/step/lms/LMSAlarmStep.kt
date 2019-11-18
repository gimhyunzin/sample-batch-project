package com.sample.batch.ticket.step.lms

import com.sample.batch.common.model.Msg
import com.sample.batch.common.property.BatchJob
import com.sample.batch.ticket.model.Ticket
import com.sample.batch.ticket.step.lms.processor.LMSAlarmProcessor
import com.sample.batch.ticket.step.lms.reader.LMSAlarmReader
import com.sample.batch.ticket.step.lms.writer.LMSAlarmWriter
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration


@Configuration
class LMSAlarmStep {
    private val logger = LoggerFactory.getLogger(LMSAlarmStep::class.java)

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    lateinit var lmsAlarmReader: LMSAlarmReader

    @Autowired
    lateinit var lmsAlarmProcessor: LMSAlarmProcessor

    @Autowired
    lateinit var lmsAlarmWriter: LMSAlarmWriter

    @Throws(Exception::class)
    fun createSMSStep(): Step {
        logger.info("*************** Create Step : " + BatchJob.TICKET_SMS_STEP_NAME.value + " ***************")

        return stepBuilderFactory.get(BatchJob.TICKET_SMS_STEP_NAME.value)
                .chunk<MutableList<Ticket>, MutableList<Msg>>(100)
                .reader(lmsAlarmReader)
                .processor(lmsAlarmProcessor)
                .writer(lmsAlarmWriter)
                .build()
    }
}