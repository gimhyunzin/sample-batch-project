package com.sample.batch.order.job

import com.sample.batch.common.property.BatchJob
import com.sample.batch.order.job.listener.AdminAlarmJobListener
import com.sample.batch.order.step.ClaimRequestAlarmKeyStep
import com.sample.batch.order.step.ClaimRequestAlarmKkoStep
import com.sample.batch.order.step.ClaimRequestAlarmLmsStep
import com.sample.batch.order.step.ClaimRequestAlarmMailStep
import org.springframework.batch.core.Job
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableBatchProcessing
class ClaimRequestAlarmJob {

    @Autowired
    lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    lateinit var claimRequestAlarmKkoStep: ClaimRequestAlarmKkoStep

    @Autowired
    lateinit var claimRequestAlarmLmsStep: ClaimRequestAlarmLmsStep

    @Autowired
    lateinit var claimRequestAlarmMailStep: ClaimRequestAlarmMailStep

    @Autowired
    lateinit var claimRequestAlarmKeyStep: ClaimRequestAlarmKeyStep

    /**
     * ================ TOTAL FLOW ================
     *
     * > [KKO STEP]
     * 1. SQL 조회
     * 2. SHARE 실행 (PUT : CLAIM_REQUEST_DATA_LIST)
     * 3. 데이터 가공 / INSERT
     * 5. SHARE 실행 (PUT : CLAIM_REQUEST_KKO_LIST)
     *
     * > [LMS STEP]
     * 1. SHARE 조회 (GET : CLAIM_REQUEST_KKO_LIST)
     * 2. 데이터 가공 / INSERT
     *
     * > [MAIL STEP]
     * 1. SHARE 조회 (GET : CLAIM_REQUEST_DATA_LIST)
     * 2. SHARE 실행 (PUT : CLAIM_REQUEST_LAST_KEY)
     * 3. 데이터 가공 / INSERT
     *
     * > [KEY STEP]
     * 1. SHARE 조회 (GET : CLAIM_REQUEST_LAST_KEY)
     * 2. 데이터 UPDATE
     *
     * ============================================
     */
    @Bean
    @Throws(Exception::class)
    fun createClaimRequestJob(): Job {
        return jobBuilderFactory.get(BatchJob.CLAIM_REQUEST_ALARM_JOB_NAME.value)
                .incrementer(RunIdIncrementer())
                .listener(AdminAlarmJobListener())
                .flow(claimRequestAlarmKkoStep.createClaimRequestKkoStep())
                .next(claimRequestAlarmLmsStep.createClaimRequestLmsStep())
                .next(claimRequestAlarmMailStep.createClaimRequestMailStep())
                .next(claimRequestAlarmKeyStep.createClaimRequestKeyStep())
                .end()
                .build()
    }
}