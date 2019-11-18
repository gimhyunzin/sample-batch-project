package com.sample.batch.order.step

import com.sample.batch.common.model.Msg
import com.sample.batch.common.property.BatchJob
import com.sample.batch.order.step.processor.OrderCompleteAlarmLmsProcessor
import com.sample.batch.order.step.reader.OrderCompleteAlarmLmsReader
import com.sample.batch.order.step.writer.OrderCompleteAlarmLmsWriter
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration


@Configuration
@EnableBatchProcessing
class OrderCompleteAlarmLmsStep {

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    lateinit var orderCompleteAlarmLmsReader: OrderCompleteAlarmLmsReader

    @Autowired
    lateinit var orderCompleteAlarmLmsWriter: OrderCompleteAlarmLmsWriter

    @Autowired
    lateinit var orderCompleteAlarmLmsProcessor: OrderCompleteAlarmLmsProcessor

    @Throws(Exception::class)
    fun createOrderCompleteLmsStep(): Step {
        return stepBuilderFactory.get(BatchJob.ORDER_COMPLETE_ALARM_LMS_STEP.value)
                .chunk<MutableList<Msg>, MutableList<Msg>>(100)
                .reader(orderCompleteAlarmLmsReader)
                .processor(orderCompleteAlarmLmsProcessor)
                .writer(orderCompleteAlarmLmsWriter)
                .build()
    }
}