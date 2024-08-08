import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class HealthRepositoryTest {

    private lateinit var stressDataSource: StressDataSource
    private lateinit var activityDataSource: ActivityDataSource
    private lateinit var sleepDataSource: SleepDataSource
    private lateinit var timeProvider: TimeProvider
    private lateinit var healthRepository: HealthRepository

    @Before
    fun setUp() {
        stressDataSource = mockk()
        activityDataSource = mockk()
        sleepDataSource = mockk()
        timeProvider = mockk()
        healthRepository = HealthRepository(stressDataSource, activityDataSource, sleepDataSource, timeProvider)
    }

    @Test
    fun `test mapToDailyStresses with multiple stresses on the same day`() {
        val stress1 = Stress(endTime = 1609473600000L) // Jan 1, 2021 04:00:00 GMT
        val stress2 = Stress(endTime = 1609477200000L) // Jan 1, 2021 05:00:00 GMT
        val stressList = listOf(stress1, stress2)

        mockkStatic(HLocalTime::class)
        every { HLocalTime.getStartOfDay(1609473600000L) } returns 1609459200000L // Start of Jan 1, 2021
        every { HLocalTime.getStartOfDay(1609477200000L) } returns 1609459200000L // Start of Jan 1, 2021

        val result = healthRepository.mapToDailyStresses(stressList)

        assertEquals(1, result.size)
        assertEquals(2, result[0].stresses.size)
        assertEquals(stress1, result[0].stresses[0])
        assertEquals(stress2, result[0].stresses[1])

        unmockkStatic(HLocalTime::class)
    }
}