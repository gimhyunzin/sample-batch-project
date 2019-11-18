import com.sample.batch.BatchApplication
import com.sample.batch.ticket.job.TicketAlarmJob
import name.falgout.jeffrey.testing.junit5.MockitoExtension
import org.junit.jupiter.api.Assertions.assertEquals
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
class TicketAlarmJobTest {

    /* **************************************** TEST CASE 내역 ***********************************************
        testTicketAlarmJob    : ticketAlarmJob Test
     *******************************************************************************************************/

    @Mock
    lateinit var jobLauncherTestUtils: JobLauncherTestUtils

    @Autowired
    lateinit var jobLauncher: JobLauncher

    @Autowired
    lateinit var ticketAlarmJob: TicketAlarmJob

    @BeforeEach
    fun init() {
        jobLauncherTestUtils = JobLauncherTestUtils()
        jobLauncherTestUtils.jobLauncher = jobLauncher
    }

    @Test
    @Rollback
    @Throws(Exception::class)
    fun testTicketAlarmJob() {
        println("*************** TEST TicketAlarmJob ***************")
        jobLauncherTestUtils.job = ticketAlarmJob.createJob()
        val jobExecution = jobLauncherTestUtils.launchJob()
        assertEquals(BatchStatus.COMPLETED, jobExecution.status)
    }

}
