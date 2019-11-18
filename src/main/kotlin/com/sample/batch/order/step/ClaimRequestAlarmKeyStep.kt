package com.sample.batch.order.step

import com.sample.batch.common.property.BatchJob
import com.sample.batch.order.step.reader.ClaimRequestAlarmKeyReader
import com.sample.batch.order.step.writer.ClaimRequestAlarmKeyWriter
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration


@Configuration
@EnableBatchProcessing
class ClaimRequestAlarmKeyStep {

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    lateinit var claimRequestAlarmKeyReader: ClaimRequestAlarmKeyReader

    @Autowired
    lateinit var claimRequestAlarmKeyWriter: ClaimRequestAlarmKeyWriter

    @Throws(Exception::class)
    fun createClaimRequestKeyStep(): Step {
        return stepBuilderFactory.get(BatchJob.CLAIM_REQUEST_ALARM_KEY_STEP.value)
                .chunk<Long, Long>(10)
                .reader(claimRequestAlarmKeyReader)
                .writer(claimRequestAlarmKeyWriter)
                .build()
    }
}