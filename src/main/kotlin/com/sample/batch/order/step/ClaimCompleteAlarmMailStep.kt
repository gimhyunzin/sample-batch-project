package com.sample.batch.order.step

import com.sample.batch.common.model.Mail
import com.sample.batch.common.property.BatchJob
import com.sample.batch.order.model.AdminAlarm
import com.sample.batch.order.step.processor.ClaimCompleteAlarmMailProcessor
import com.sample.batch.order.step.reader.ClaimCompleteAlarmMailReader
import com.sample.batch.order.step.writer.ClaimCompleteAlarmMailWriter
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration


@Configuration
@EnableBatchProcessing
class ClaimCompleteAlarmMailStep {

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    lateinit var claimCompleteAlarmMailReader: ClaimCompleteAlarmMailReader

    @Autowired
    lateinit var claimCompleteAlarmMailWriter: ClaimCompleteAlarmMailWriter

    @Autowired
    lateinit var claimCompleteAlarmMailProcessor: ClaimCompleteAlarmMailProcessor

    @Throws(Exception::class)
    fun createClaimCompleteMailStep(): Step {
        return stepBuilderFactory.get(BatchJob.CLAIM_COMPLETE_ALARM_MAIL_STEP.value)
                .chunk<MutableList<AdminAlarm>, MutableList<Mail>>(100)
                .reader(claimCompleteAlarmMailReader)
                .processor(claimCompleteAlarmMailProcessor)
                .writer(claimCompleteAlarmMailWriter)
                .build()
    }
}