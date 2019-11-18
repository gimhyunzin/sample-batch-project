package com.sample.batch.order.step

import com.sample.batch.common.model.Mail
import com.sample.batch.common.property.BatchJob
import com.sample.batch.order.model.AdminAlarm
import com.sample.batch.order.step.processor.ClaimRequestAlarmMailProcessor
import com.sample.batch.order.step.reader.ClaimRequestAlarmMailReader
import com.sample.batch.order.step.writer.ClaimRequestAlarmMailWriter
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration


@Configuration
@EnableBatchProcessing
class ClaimRequestAlarmMailStep {

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    lateinit var claimRequestAlarmMailReader: ClaimRequestAlarmMailReader

    @Autowired
    lateinit var claimRequestAlarmMailWriter: ClaimRequestAlarmMailWriter

    @Autowired
    lateinit var claimRequestAlarmMailProcessor: ClaimRequestAlarmMailProcessor

    @Throws(Exception::class)
    fun createClaimRequestMailStep(): Step {
        return stepBuilderFactory.get(BatchJob.CLAIM_REQUEST_ALARM_MAIL_STEP.value)
                .chunk<MutableList<AdminAlarm>, MutableList<Mail>>(100)
                .reader(claimRequestAlarmMailReader)
                .processor(claimRequestAlarmMailProcessor)
                .writer(claimRequestAlarmMailWriter)
                .build()
    }
}