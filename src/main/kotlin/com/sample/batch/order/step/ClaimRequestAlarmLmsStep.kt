package com.sample.batch.order.step

import com.sample.batch.common.model.Msg
import com.sample.batch.common.property.BatchJob
import com.sample.batch.order.step.processor.ClaimRequestAlarmLmsProcessor
import com.sample.batch.order.step.reader.ClaimRequestAlarmLmsReader
import com.sample.batch.order.step.writer.ClaimRequestAlarmLmsWriter
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration


@Configuration
@EnableBatchProcessing
class ClaimRequestAlarmLmsStep {

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    lateinit var claimRequestAlarmLmsReader: ClaimRequestAlarmLmsReader

    @Autowired
    lateinit var claimRequestAlarmLmsWriter: ClaimRequestAlarmLmsWriter

    @Autowired
    lateinit var claimRequestAlarmLmsProcessor: ClaimRequestAlarmLmsProcessor

    @Throws(Exception::class)
    fun createClaimRequestLmsStep(): Step {
        return stepBuilderFactory.get(BatchJob.CLAIM_REQUEST_ALARM_LMS_STEP.value)
                .chunk<MutableList<Msg>, MutableList<Msg>>(100)
                .reader(claimRequestAlarmLmsReader)
                .processor(claimRequestAlarmLmsProcessor)
                .writer(claimRequestAlarmLmsWriter)
                .build()
    }
}