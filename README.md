
    @Test
    fun testHasDataForDate() = runTest {
        val utcStartTime = 1627550710000L
        val expectedResult = true
        
        coEvery { dataSource.hasDataForDate(utcStartTime) } returns expectedResult

        val result = myHeartDataRepository.hasDataForDate(utcStartTime)
        
        assertEquals(expectedResult, result)
        coVerify { dataSource.hasDataForDate(utcStartTime) }
        confirmVerified(dataSource)
    }
