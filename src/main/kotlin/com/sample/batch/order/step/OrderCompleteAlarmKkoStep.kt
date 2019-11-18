package com.sample.batch.order.step

import com.sample.batch.common.model.Msg
import com.sample.batch.common.property.BatchJob
import com.sample.batch.order.model.AdminAlarm
import com.sample.batch.order.step.processor.OrderCompleteAlarmKkoProcessor
import com.sample.batch.order.step.reader.OrderCompleteAlarmKkoReader
import com.sample.batch.order.step.writer.OrderCompleteAlarmKkoWriter
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration


@Configuration
@EnableBatchProcessing
class OrderCompleteAlarmKkoStep {

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    lateinit var orderCompleteAlarmKkoReader: OrderCompleteAlarmKkoReader

    @Autowired
    lateinit var orderCompleteAlarmKkoWriter: OrderCompleteAlarmKkoWriter

    @Autowired
    lateinit var orderCompleteAlarmKkoProcessor: OrderCompleteAlarmKkoProcessor

    @Throws(Exception::class)
    fun createOrderCompleteKkoStep(): Step {
        return stepBuilderFactory.get(BatchJob.ORDER_COMPLETE_ALARM_KKO_STEP.value)
                .chunk<MutableList<AdminAlarm>, MutableList<Msg>>(100)
                .reader(orderCompleteAlarmKkoReader)
                .processor(orderCompleteAlarmKkoProcessor)
                .writer(orderCompleteAlarmKkoWriter)
                .build()
    }
}