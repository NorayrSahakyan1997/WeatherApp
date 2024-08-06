
     override suspend fun updateData(
        uuid: String,
        entry: MapMeasurementEntity
    ): HealthResultHolder.BaseResult {
        val updateRequest = HealthDataResolver.UpdateRequest.Builder()
            .setDataType(MapMeasurement.HEALTH_DATA_TYPE)
            .setFilter(HealthDataResolver.Filter.eq(MapMeasurement.UUID, uuid))
            .setHealthData(entry.makeHealthData())
            .build()
        return update(updateRequest).await()
    }
