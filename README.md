class MyHeartDataRepositoryTest {

    private val dataSource = mockk<TrackerMapDataSource>()
    private lateinit var myHeartDataRepository: MyHeartDataRepositoryImpl


    @Before
    fun setUp() {
        myHeartDataRepository = MyHeartDataRepositoryImpl(dataSource = dataSource)
    }

    @Test
    fun testResetBaseline() = runTest {
        coEvery { dataSource.getLastEntry() } returns null
        coEvery { dataSource.getLastEntry()?.type } returns null
        coEvery { dataSource.insertData(any()) } returns HealthResultHolder.BaseResult(
            STATUS_SUCCESSFUL,
            1
        )

        myHeartDataRepository.resetBaseline()
        coVerify { dataSource.getLastEntry() }
        coVerify { dataSource.insertData(any()) }
        confirmVerified(dataSource)
    }

    @Test
    fun testDeleteDataInDbWithIds() = runTest {
    }

    @Test
    fun testHasDataForDate() = runTest {

    }
}
