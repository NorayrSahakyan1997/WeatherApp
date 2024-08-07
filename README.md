internal class MapFeatureRepositoryImpl(
    private val featureManager: FeatureManager,
    private val context: Context,
) : MapFeatureRepository {
    override fun isSupported(): Boolean {
        val mainSwitch = featureManager.isSupported(FeatureList.Key.TRACKER_MAP_FEATURE_ON)
        if (!mainSwitch) {
            return false
        }
        if (featureManager.isSupported(FeatureList.Key.TRACKER_MAP_DISABLE_COUNTRY_LIMITED)) {
            return true
        }
        return CSCUtils.isUSModel(context) || CSCUtils.isKoreaModel(context)
    }

    override fun isDevMode() = featureManager.isSupported(FeatureList.Key.TRACKER_MAP_DEV_MODE)
}
