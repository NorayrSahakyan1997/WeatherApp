
   @Test
    fun `test queryDailyStresses`() = runBlocking {
        val startTime = 1609459200000L // Jan 1, 2021 00:00:00 GMT
        val endTime = 1609545600000L // Jan 2, 2021 00:00:00 GMT
        val endTimeInChronologicalOrder = true
        val limit = 10

        val stress = Stress(endTime = 1609473600000L) // Jan 1, 2021 04:00:00 GMT
        val stressList = listOf(stress)

        coEvery { stressDataSource.queryStresses(startTime, endTime, endTimeInChronologicalOrder, limit) } returns stressList
        every { timeProvider.localTimeNow() } returns LocalTime.NOON

        val result = healthRepository.queryDailyStresses(startTime, endTime, endTimeInChronologicalOrder, limit)

        assertEquals(1, result.size)
        coVerify { stressDataSource.queryStresses(startTime, endTime, endTimeInChronologicalOrder, limit) }
        confirmVerified(stressDataSource)
    }

    @Test
    fun `test queryDailyActivities`() = runBlocking {
        val startTime = 1609459200000L // Jan 1, 2021 00:00:00 GMT
        val endTime = 1609545600000L // Jan 2, 2021 00:00:00 GMT
        val endTimeInChronologicalOrder = true
        val limit = 10

        val activity = Activity(time = 1609473600000L, activeTime = 3600000L, goal = 60) // Jan 1, 2021 04:00:00 GMT, 1 hour active time
        val activityList = listOf(activity)

        coEvery { activityDataSource.queryActivities(startTime, endTime, endTimeInChronologicalOrder, limit) } returns activityList

        val result = healthRepository.queryDailyActivities(startTime, endTime, endTimeInChronologicalOrder, limit)

        assertEquals(1, result.size)
        assertEquals(DailyActivity(1609473600000L, 60, 60), result[0])
        coVerify { activityDataSource.queryActivities(startTime, endTime, endTimeInChronologicalOrder, limit) }
        confirmVerified(activityDataSource)
    }

    @Test
    fun `test querySleepsForDay`() = runBlocking {
        val startTime = 1609459200000L // Jan 1, 2021 00:00:00 GMT
        val sleep = Sleep(time = 1609459200000L, duration = 28800000L) // 8 hours sleep

        coEvery { sleepDataSource.querySleepsForDay(startTime) } returns sleep

        val result = healthRepository.querySleepsForDay(startTime)

        assertEquals(DailySleep(1609459200000L, 480), result)
        coVerify { sleepDataSource.querySleepsForDay(startTime) }
        confirmVerified(sleepDataSource)
    }

    @Test
    fun `test querySleepsForDay returns null`() = runBlocking {
        val startTime = 1609459200000L // Jan 1, 2021 00:00:00 GMT

        coEvery { sleepDataSource.querySleepsForDay(startTime) } returns null

        val result = healthRepository.querySleepsForDay(startTime)

        assertNull(result)
        coVerify { sleepDataSource.querySleepsForDay(startTime) }
        confirmVerified(sleepDataSource)
    }
