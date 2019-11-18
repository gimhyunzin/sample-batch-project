package com.sample.batch.order.step

import com.sample.batch.common.model.Msg
import com.sample.batch.common.property.BatchJob
import com.sample.batch.order.model.AdminAlarm
import com.sample.batch.order.step.processor.ClaimCompleteAlarmKkoProcessor
import com.sample.batch.order.step.reader.ClaimCompleteAlarmKkoReader
import com.sample.batch.order.step.writer.ClaimCompleteAlarmKkoWriter
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration


@Configuration
@EnableBatchProcessing
class ClaimCompleteAlarmKkoStep {

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    lateinit var claimCompleteAlarmKkoReader: ClaimCompleteAlarmKkoReader

    @Autowired
    lateinit var claimCompleteAlarmKkoWriter: ClaimCompleteAlarmKkoWriter

    @Autowired
    lateinit var claimCompleteAlarmKkoProcessor: ClaimCompleteAlarmKkoProcessor

    @Throws(Exception::class)
    fun createClaimCompleteKkoStep(): Step {
        return stepBuilderFactory.get(BatchJob.CLAIM_COMPLETE_ALARM_KKO_STEP.value)
                .chunk<MutableList<AdminAlarm>, MutableList<Msg>>(100)
                .reader(claimCompleteAlarmKkoReader)
                .processor(claimCompleteAlarmKkoProcessor)
                .writer(claimCompleteAlarmKkoWriter)
                .build()
    }
}