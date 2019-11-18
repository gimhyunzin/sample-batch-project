package com.sample.batch.order.job

import com.sample.batch.common.property.BatchJob
import com.sample.batch.order.job.listener.AdminAlarmJobListener
import com.sample.batch.order.step.OrderCompleteAlarmKeyStep
import com.sample.batch.order.step.OrderCompleteAlarmKkoStep
import com.sample.batch.order.step.OrderCompleteAlarmLmsStep
import com.sample.batch.order.step.OrderCompleteAlarmMailStep
import org.springframework.batch.core.Job
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableBatchProcessing
class OrderCompleteAlarmJob {

    @Autowired
    lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    lateinit var orderCompleteAlarmKkoStep: OrderCompleteAlarmKkoStep

    @Autowired
    lateinit var orderCompleteAlarmLmsStep: OrderCompleteAlarmLmsStep

    @Autowired
    lateinit var orderCompleteAlarmMailStep: OrderCompleteAlarmMailStep

    @Autowired
    lateinit var orderCompleteAlarmKeyStep: OrderCompleteAlarmKeyStep

    /**
     * ================ TOTAL FLOW ================
     *
     * > [KKO STEP]
     * 1. SQL 조회
     * 2. SHARE 실행 (PUT : ORDER_COMPLETE_DATA_LIST)
     * 3. 데이터 가공 / INSERT
     * 5. SHARE 실행 (PUT : ORDER_COMPLETE_KKO_LIST)
     *
     * > [LMS STEP]
     * 1. SHARE 조회 (GET : ORDER_COMPLETE_KKO_LIST)
     * 2. 데이터 가공 / INSERT
     *
     * > [MAIL STEP]
     * 1. SHARE 조회 (GET : ORDER_COMPLETE_DATA_LIST)
     * 2. SHARE 실행 (PUT : ORDER_COMPLETE_LAST_KEY)
     * 3. 데이터 가공 / INSERT
     *
     * > [KEY STEP]
     * 1. SHARE 조회 (GET : ORDER_COMPLETE_LAST_KEY)
     * 2. 데이터 UPDATE
     *
     * ============================================
     */
    @Bean
    @Throws(Exception::class)
    fun createOrderCompleteJob(): Job {
        return jobBuilderFactory.get(BatchJob.ORDER_COMPLETE_ALARM_JOB_NAME.value)
                .incrementer(RunIdIncrementer())
                .listener(AdminAlarmJobListener())
                .flow(orderCompleteAlarmKkoStep.createOrderCompleteKkoStep())
                .next(orderCompleteAlarmLmsStep.createOrderCompleteLmsStep())
                .next(orderCompleteAlarmMailStep.createOrderCompleteMailStep())
                .next(orderCompleteAlarmKeyStep.createOrderCompleteKeyStep())
                .end()
                .build()
    }
}