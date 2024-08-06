   internal class MyHeartDataRepositoryImpl(
    private val dataSource: TrackerMapDataSource,
) : MyHeartDataRepository {
    override fun getMeasurementsFlow(
        startTime: Long,
        endTimeInChronologicalOrder: Boolean,
        limit: Int?
    ) = dataSource.getMeasurementsFlow(
        startTime = startTime,
        endTimeInChronologicalOrder = endTimeInChronologicalOrder,
        limit = limit
    )

    override suspend fun hasDataForDate(utcStartTime: Long) =
        dataSource.hasDataForDate(utcStartTime)

    override fun isCompleteBaselineOrMeasurementEverCreatedFlow() =
        dataSource.isCompleteBaselineOrMeasurementEverCreatedFlow()

    override suspend fun resetBaseline() {
        val lastEntry = dataSource.getLastEntry()
        val currentTime = System.currentTimeMillis()
        val timezoneOffset = TimeZone.getDefault().getOffset(currentTime)
        val data = MapMeasurementEntity(
            startTime = currentTime,
            endTime = currentTime,
            timeOffset = timezoneOffset.toLong(),
            type = MapDataType.RESET_BASELINE,
            measurement = null,
        )
        if (lastEntry?.type == MapDataType.RESET_BASELINE) {
            dataSource.updateData(lastEntry.dataUuid, data)
            return
        }
        dataSource.insertData(data)
    }

    override suspend fun deleteDataInDbWithIds(uuidList: List<String>) =
        dataSource.deleteDataInDbWithIds(uuidList)
}
