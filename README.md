@RunWith(SDK34RobolectricGradleTestRunner::class)
@Config(sdk = [34], shadows = [ShadowContextHolder::class])
class MapDailyActivityDataSourceTest {
    private val startTime = HLocalTime.getStartOfToday()
    private val endTime = HLocalTime.getEndOfToday()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        TestUtility.initRobolectricTestEnvironment()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun test_daily_active_time_return_correctly() = runTest {

        val activeTimeMillis = 30 * 60 * 1000L
        val goalTimeMins = 90

        val mockCursor = mockk<Cursor>(relaxed = true).apply {
            every { count } returns 1
            every { close() } returns Unit
            every { moveToFirst() } returns true
            every { moveToNext() } returns true andThen false
            every { getColumnIndex(ActivityDataConstants.DaySummary.DAY_TIME) } returns 0
            every { getLong(getColumnIndex(ActivityDataConstants.DaySummary.DAY_TIME)) } returns startTime
            every { getLong(getColumnIndex(ActivityDataConstants.DaySummary.ACTIVE_TIME)) } returns activeTimeMillis
            every { getInt(getColumnIndex(ActivityDataConstants.DaySummary.GOAL)) } returns goalTimeMins
            every { getInt(getColumnIndex(ActivityDataConstants.DaySummary.DAILY_ACTIVITY_GOAL)) } returns goalTimeMins
        }

        val mockReadResult = mockk<ReadResult>(relaxed = true).apply {
            every { status } returns HealthResultHolder.BaseResult.STATUS_SUCCESSFUL
            every { resultCursor } returns mockCursor
        }

        mockkStatic(RecoverableHealthDataResolver::class)
        coEvery { RecoverableHealthDataResolver.read(any()) } returns Single.just(mockReadResult)

        val sutActivityDataSource = TrackerActivityDataSourceImpl()
        val activityList = sutActivityDataSource.queryActivities(startTime, endTime)

        assertEquals("size is not correct", activityList.size, 1)
        assertEquals("activeTime is not correct", activityList.first().activeTime, activeTimeMillis)
        assertEquals("goal is not correct", activityList.first().goal, goalTimeMins)
    }

}
