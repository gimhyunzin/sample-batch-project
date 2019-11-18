package com.sample.batch.order.step

import com.sample.batch.common.property.BatchJob
import com.sample.batch.order.step.reader.ClaimCompleteAlarmKeyReader
import com.sample.batch.order.step.writer.ClaimCompleteAlarmKeyWriter
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration


@Configuration
@EnableBatchProcessing
class ClaimCompleteAlarmKeyStep {

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    lateinit var claimCompleteAlarmKeyReader : ClaimCompleteAlarmKeyReader

    @Autowired
    lateinit var claimCompleteAlarmKeyWriter : ClaimCompleteAlarmKeyWriter

    @Throws(Exception::class)
    fun createClaimCompleteKeyStep(): Step {
        return stepBuilderFactory.get(BatchJob.CLAIM_COMPLETE_ALARM_KEY_STEP.value)
                .chunk<Long, Long>(10)
                .reader(claimCompleteAlarmKeyReader)
                .writer(claimCompleteAlarmKeyWriter)
                .build()
    }
}