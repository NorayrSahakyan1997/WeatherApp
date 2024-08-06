
    @Test
    fun testIsCompleteBaselineOrMeasurementEverCreatedFlow() = runTest {
        // Mocking RecoverableHealthDataObserver.addObserver
        val observerMock = mockk<RecoverableHealthDataObserver>()
        val healthDataType = MapMeasurement.HEALTH_DATA_TYPE

        // Mock the addObserver method to return a Flowable
        every { observerMock.addObserver(healthDataType) } returns
            Flowable.just(true).toFlowable(BackpressureStrategy.LATEST)

        // Mock the static method call
        mockkObject(RecoverableHealthDataObserver)
        every { RecoverableHealthDataObserver.addObserver(healthDataType) } returns
            observerMock.addObserver(healthDataType)

        // Mock the queryHasCompletedBaselineOrMeasurement method
        every { myHeartDataRepository.queryHasCompletedBaselineOrMeasurement() } returns true

        // Convert Flowable to Flow and test the emissions
        myHeartDataRepository.isCompleteBaselineOrMeasurementEverCreatedFlow().test {
            // Expect the initial null emission
            assertEquals(null, awaitItem())
            // Expect the mapped result from queryHasCompletedBaselineOrMeasurement
            assertEquals(true, awaitItem())
            awaitComplete()
        }

        // Verify the interactions
        verify { RecoverableHealthDataObserver.addObserver(healthDataType) }
        confirmVerified(observerMock)
    }
}
