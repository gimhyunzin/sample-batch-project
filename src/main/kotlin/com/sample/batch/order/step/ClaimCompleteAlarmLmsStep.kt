package com.sample.batch.order.step

import com.sample.batch.common.model.Msg
import com.sample.batch.common.property.BatchJob
import com.sample.batch.order.step.processor.ClaimCompleteAlarmLmsProcessor
import com.sample.batch.order.step.reader.ClaimCompleteAlarmLmsReader
import com.sample.batch.order.step.writer.ClaimCompleteAlarmLmsWriter
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration


@Configuration
@EnableBatchProcessing
class ClaimCompleteAlarmLmsStep {

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    lateinit var claimCompleteAlarmLmsReader: ClaimCompleteAlarmLmsReader

    @Autowired
    lateinit var claimCompleteAlarmLmsWriter: ClaimCompleteAlarmLmsWriter

    @Autowired
    lateinit var claimCompleteAlarmLmsProcessor: ClaimCompleteAlarmLmsProcessor

    @Throws(Exception::class)
    fun createClaimCompleteLmsStep(): Step {
        return stepBuilderFactory.get(BatchJob.CLAIM_COMPLETE_ALARM_LMS_STEP.value)
                .chunk<MutableList<Msg>, MutableList<Msg>>(100)
                .reader(claimCompleteAlarmLmsReader)
                .processor(claimCompleteAlarmLmsProcessor)
                .writer(claimCompleteAlarmLmsWriter)
                .build()
    }
}