package com.sample.batch.order.job

import com.sample.batch.common.property.BatchJob
import com.sample.batch.order.job.listener.AdminAlarmJobListener
import com.sample.batch.order.step.ClaimCompleteAlarmKeyStep
import com.sample.batch.order.step.ClaimCompleteAlarmKkoStep
import com.sample.batch.order.step.ClaimCompleteAlarmLmsStep
import com.sample.batch.order.step.ClaimCompleteAlarmMailStep
import org.springframework.batch.core.Job
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableBatchProcessing
class ClaimCompleteAlarmJob {

    @Autowired
    lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    lateinit var claimCompleteAlarmKkoStep: ClaimCompleteAlarmKkoStep

    @Autowired
    lateinit var claimCompleteAlarmLmsStep: ClaimCompleteAlarmLmsStep

    @Autowired
    lateinit var claimCompleteAlarmMailStep: ClaimCompleteAlarmMailStep

    @Autowired
    lateinit var claimCompleteAlarmKeyStep: ClaimCompleteAlarmKeyStep

    /**
     * ================ TOTAL FLOW ================
     *
     * > [KKO STEP]
     * 1. SQL 조회
     * 2. SHARE 실행 (PUT : CLAIM_COMPLETE_DATA_LIST)
     * 3. 데이터 가공 / INSERT
     * 5. SHARE 실행 (PUT : CLAIM_COMPLETE_KKO_LIST)
     *
     * > [LMS STEP]
     * 1. SHARE 조회 (GET : CLAIM_COMPLETE_KKO_LIST)
     * 2. 데이터 가공 / INSERT
     *
     * > [MAIL STEP]
     * 1. SHARE 조회 (GET : CLAIM_COMPLETE_DATA_LIST)
     * 2. SHARE 실행 (PUT : CLAIM_COMPLETE_LAST_KEY)
     * 3. 데이터 가공 / INSERT
     *
     * > [KEY STEP]
     * 1. SHARE 조회 (GET : CLAIM_COMPLETE_LAST_KEY)
     * 2. 데이터 UPDATE
     *
     * ============================================
     */
    @Bean
    @Throws(Exception::class)
    fun createClaimCompleteJob(): Job {
        return jobBuilderFactory.get(BatchJob.CLAIM_COMPLETE_ALARM_JOB_NAME.value)
                .incrementer(RunIdIncrementer())
                .listener(AdminAlarmJobListener())
                .flow(claimCompleteAlarmKkoStep.createClaimCompleteKkoStep())
                .next(claimCompleteAlarmLmsStep.createClaimCompleteLmsStep())
                .next(claimCompleteAlarmMailStep.createClaimCompleteMailStep())
                .next(claimCompleteAlarmKeyStep.createClaimCompleteKeyStep())
                .end()
                .build()
    }
}