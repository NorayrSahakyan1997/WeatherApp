
    @Test
    fun testGetMeasurementsFlow() = runTest {
        // Define input parameters
        val startTime = 1627550710000L
        val endTimeInChronologicalOrder = true
        val limit = 10
        
        // Define the expected Flow
        val expectedFlow: Flow<List<MapMeasurementEntity>> = flowOf(listOf())
        
        // Mock the dataSource.getMeasurementsFlow method
        every { dataSource.getMeasurementsFlow(startTime, endTimeInChronologicalOrder, limit) } returns expectedFlow

        // Call the method under test
        val resultFlow = myHeartDataRepository.getMeasurementsFlow(startTime, endTimeInChronologicalOrder, limit)
        
        // Assert that the result matches the expected flow
        assertEquals(expectedFlow, resultFlow)
        
        // Verify that the dataSource.getMeasurementsFlow was called with the correct parameters
        verify { dataSource.getMeasurementsFlow(startTime, endTimeInChronologicalOrder, limit) }
        confirmVerified(dataSource)
    }
