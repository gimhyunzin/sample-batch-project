package com.sample.batch.order.step

import com.sample.batch.common.property.BatchJob
import com.sample.batch.order.step.reader.OrderCompleteAlarmKeyReader
import com.sample.batch.order.step.writer.OrderCompleteAlarmKeyWriter
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration


@Configuration
@EnableBatchProcessing
class OrderCompleteAlarmKeyStep {

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    lateinit var orderCompleteAlarmKeyReader: OrderCompleteAlarmKeyReader

    @Autowired
    lateinit var orderCompleteAlarmKeyWriter: OrderCompleteAlarmKeyWriter

    @Throws(Exception::class)
    fun createOrderCompleteKeyStep(): Step {
        return stepBuilderFactory.get(BatchJob.ORDER_COMPLETE_ALARM_KEY_STEP.value)
                .chunk<Long, Long>(10)
                .reader(orderCompleteAlarmKeyReader)
                .writer(orderCompleteAlarmKeyWriter)
                .build()
    }
}