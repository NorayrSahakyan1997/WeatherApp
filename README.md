 private fun checkCurrentTimeWithinRange(
        localTime0: LocalTime,
        localTime1: LocalTime,
    ): Boolean {
        val currentTime = timeProvider.localTimeNow()
        return currentTime.isBefore(localTime0).not() && currentTime.isBefore(localTime1)
    }
