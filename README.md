}

override suspend fun queryDailyStresses(
    startTime: Long,
    endTime: Long,
    endTimeInChronologicalOrder: Boolean,
    limit: Int?
): List<DailyStress> {
    return mapToDailyStresses(
        stressDataSource.queryStresses(
            startTime,
            endTime,
            endTimeInChronologicalOrder,
            limit
        )
    )
}

private fun mapToDailyStresses(dataList: List<Stress>?): List<DailyStress> {
    val dailyStressMap = mutableMapOf<Long, DailyStress>()
    LOG.d(TAG, " mapToDailyStresses ${dataList?.joinToString()}")
    dataList?.forEach {
        val startOfDay = HLocalTime.getStartOfDay(it.endTime)
        if (!dailyStressMap.containsKey(startOfDay)) {
            dailyStressMap[startOfDay] = DailyStress(
                dayTime = startOfDay,
                stresses = arrayListOf(it)
            )
        } else {
            (dailyStressMap[startOfDay]?.stresses as ArrayList).add(it)
        }
    }

    return dailyStressMap.values.toList()
}

override suspend fun queryDailyActivities(
    startTime: Long,
    endTime: Long,
    endTimeInChronologicalOrder: Boolean,
    limit: Int?
): List<DailyActivity> {
    return activityDataSource.queryActivities(
        startTime,
        endTime,
        endTimeInChronologicalOrder,
        limit
    ).map {
        LOG.d(TAG, " mapped dailyActivity dayTime:  ${HLocalTime.toStringForLog(it.time)}")
        DailyActivity(
            dayTime = it.time,
            activeMinutes = HTimeUnit.millisToMinutes(it.activeTime),
            goalMinutes = it.goal
        )
    }.toList()
}

override suspend fun querySleepsForDay(
    startTime: Long
): DailySleep? {
    sleepDataSource.querySleepsForDay(startTime)?.let {
        return DailySleep(
            dayTime = it.time,
            durationMinutes = HTimeUnit.millisToMinutes(it.duration)
        )
    }

    return null
}
