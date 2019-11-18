package com.sample.batch.order.step

import com.sample.batch.common.model.Msg
import com.sample.batch.common.property.BatchJob
import com.sample.batch.order.model.AdminAlarm
import com.sample.batch.order.step.processor.ClaimRequestAlarmKkoProcessor
import com.sample.batch.order.step.reader.ClaimRequestAlarmKkoReader
import com.sample.batch.order.step.writer.ClaimRequestAlarmKkoWriter
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration


@Configuration
@EnableBatchProcessing
class ClaimRequestAlarmKkoStep {

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    lateinit var claimRequestAlarmKkoReader: ClaimRequestAlarmKkoReader

    @Autowired
    lateinit var claimRequestAlarmKkoWriter: ClaimRequestAlarmKkoWriter

    @Autowired
    lateinit var claimRequestAlarmKkoProcessor: ClaimRequestAlarmKkoProcessor

    @Throws(Exception::class)
    fun createClaimRequestKkoStep(): Step {
        return stepBuilderFactory.get(BatchJob.CLAIM_REQUEST_ALARM_KKO_STEP.value)
                .chunk<MutableList<AdminAlarm>, MutableList<Msg>>(100)
                .reader(claimRequestAlarmKkoReader)
                .processor(claimRequestAlarmKkoProcessor)
                .writer(claimRequestAlarmKkoWriter)
                .build()
    }
}