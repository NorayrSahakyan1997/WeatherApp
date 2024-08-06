
      override fun isCompleteBaselineOrMeasurementEverCreatedFlow() =
        RecoverableHealthDataObserver.addObserver(MapMeasurement.HEALTH_DATA_TYPE)
            .toFlowable(BackpressureStrategy.LATEST)
            .asFlow()
            .onStart { emit(null) } // just to trigger loading from db
            .map { queryHasCompletedBaselineOrMeasurement() }
