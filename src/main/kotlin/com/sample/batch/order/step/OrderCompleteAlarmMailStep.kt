package com.sample.batch.order.step

import com.sample.batch.common.model.Mail
import com.sample.batch.common.property.BatchJob
import com.sample.batch.order.model.AdminAlarm
import com.sample.batch.order.step.processor.OrderCompleteAlarmMailProcessor
import com.sample.batch.order.step.reader.OrderCompleteAlarmMailReader
import com.sample.batch.order.step.writer.OrderCompleteAlarmMailWriter
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration


@Configuration
@EnableBatchProcessing
class OrderCompleteAlarmMailStep {

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    lateinit var orderCompleteAlarmMailReader: OrderCompleteAlarmMailReader

    @Autowired
    lateinit var orderCompleteAlarmMailWriter: OrderCompleteAlarmMailWriter

    @Autowired
    lateinit var orderCompleteAlarmMailProcessor: OrderCompleteAlarmMailProcessor

    @Throws(Exception::class)
    fun createOrderCompleteMailStep(): Step {
        return stepBuilderFactory.get(BatchJob.ORDER_COMPLETE_ALARM_MAIL_STEP.value)
                .chunk<MutableList<AdminAlarm>, MutableList<Mail>>(100)
                .reader(orderCompleteAlarmMailReader)
                .processor(orderCompleteAlarmMailProcessor)
                .writer(orderCompleteAlarmMailWriter)
                .build()
    }
}