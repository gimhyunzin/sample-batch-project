package com.sample.batch.order

import com.sample.batch.BatchApplication
import com.sample.batch.order.job.OrderCompleteAlarmJob
import name.falgout.jeffrey.testing.junit5.MockitoExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class, MockitoExtension::class)
@SpringBootTest(classes = [BatchApplication::class])
class OrderCompleteAlarmJobTest {

    /* **************************************** TEST CASE 내역 ***********************************************
        orderCompleteAlarmJobTest    : orderCompleteAlarmJob Test
     *******************************************************************************************************/

    @Mock
    lateinit var jobLauncherTestUtils: JobLauncherTestUtils

    @Autowired
    lateinit var jobLauncher: JobLauncher

    @Autowired
    lateinit var orderCompleteAlarmJob: OrderCompleteAlarmJob

    @BeforeEach
    fun init() {
        jobLauncherTestUtils = JobLauncherTestUtils()
        jobLauncherTestUtils.jobLauncher = jobLauncher
    }

    @Test
    @Rollback
    @Throws(Exception::class)
    fun testOrderCompleteAlarmJob() {
        println("*************** TEST OrderCompleteAlarmJob ***************")
        jobLauncherTestUtils.job = orderCompleteAlarmJob.createOrderCompleteJob()
        val jobExecution = jobLauncherTestUtils.launchJob()
        Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.status)
    }

}