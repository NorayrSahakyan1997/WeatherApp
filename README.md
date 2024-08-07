import android.content.Context
import io.mockk.*
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MapFeatureRepositoryImplTest {

    private val featureManager = mockk<FeatureManager>()
    private val context = mockk<Context>()
    private val repository = MapFeatureRepositoryImpl(featureManager, context)

    @Test
    fun `test isSupported when main switch is off`() {
        every { featureManager.isSupported(FeatureList.Key.TRACKER_MAP_FEATURE_ON) } returns false

        val result = repository.isSupported()

        assertFalse(result)
        verify { featureManager.isSupported(FeatureList.Key.TRACKER_MAP_FEATURE_ON) }
        confirmVerified(featureManager)
    }

    @Test
    fun `test isSupported when main switch is on and country limit disabled`() {
        every { featureManager.isSupported(FeatureList.Key.TRACKER_MAP_FEATURE_ON) } returns true
        every { featureManager.isSupported(FeatureList.Key.TRACKER_MAP_DISABLE_COUNTRY_LIMITED) } returns true

        val result = repository.isSupported()

        assertTrue(result)
        verify {
            featureManager.isSupported(FeatureList.Key.TRACKER_MAP_FEATURE_ON)
            featureManager.isSupported(FeatureList.Key.TRACKER_MAP_DISABLE_COUNTRY_LIMITED)
        }
        confirmVerified(featureManager)
    }

    @Test
    fun `test isSupported when main switch is on and country limit enabled but in US model`() {
        every { featureManager.isSupported(FeatureList.Key.TRACKER_MAP_FEATURE_ON) } returns true
        every { featureManager.isSupported(FeatureList.Key.TRACKER_MAP_DISABLE_COUNTRY_LIMITED) } returns false
        mockkStatic(CSCUtils::class)
        every { CSCUtils.isUSModel(context) } returns true
        every { CSCUtils.isKoreaModel(context) } returns false

        val result = repository.isSupported()

        assertTrue(result)
        verify {
            featureManager.isSupported(FeatureList.Key.TRACKER_MAP_FEATURE_ON)
            featureManager.isSupported(FeatureList.Key.TRACKER_MAP_DISABLE_COUNTRY_LIMITED)
            CSCUtils.isUSModel(context)
        }
        confirmVerified(featureManager)
        unmockkStatic(CSCUtils::class)
    }

    @Test
    fun `test isSupported when main switch is on and country limit enabled but in Korea model`() {
        every { featureManager.isSupported(FeatureList.Key.TRACKER_MAP_FEATURE_ON) } returns true
        every { featureManager.isSupported(FeatureList.Key.TRACKER_MAP_DISABLE_COUNTRY_LIMITED) } returns false
        mockkStatic(CSCUtils::class)
        every { CSCUtils.isUSModel(context) } returns false
        every { CSCUtils.isKoreaModel(context) } returns true

        val result = repository.isSupported()

        assertTrue(result)
        verify {
            featureManager.isSupported(FeatureList.Key.TRACKER_MAP_FEATURE_ON)
            featureManager.isSupported(FeatureList.Key.TRACKER_MAP_DISABLE_COUNTRY_LIMITED)
            CSCUtils.isKoreaModel(context)
        }
        confirmVerified(featureManager)
        unmockkStatic(CSCUtils::class)
    }

    @Test
    fun `test isSupported when main switch is on and country limit enabled but not in US or Korea model`() {
        every { featureManager.isSupported(FeatureList.Key.TRACKER_MAP_FEATURE_ON) } returns true
        every { featureManager.isSupported(FeatureList.Key.TRACKER_MAP_DISABLE_COUNTRY_LIMITED) } returns false
        mockkStatic(CSCUtils::class)
        every { CSCUtils.isUSModel(context) } returns false
        every { CSCUtils.isKoreaModel(context) } returns false

        val result = repository.isSupported()

        assertFalse(result)
        verify {
            featureManager.isSupported(FeatureList.Key.TRACKER_MAP_FEATURE_ON)
            featureManager.isSupported(FeatureList.Key.TRACKER_MAP_DISABLE_COUNTRY_LIMITED)
            CSCUtils.isUSModel(context)
            CSCUtils.isKoreaModel(context)
        }
        confirmVerified(featureManager)
        unmockkStatic(CSCUtils::class)
    }

    @Test
    fun `test isDevMode`() {
        every { featureManager.isSupported(FeatureList.Key.TRACKER_MAP_DEV_MODE) } returns true

        val result = repository.isDevMode()

        assertTrue(result)
        verify { featureManager.isSupported(FeatureList.Key.TRACKER_MAP_DEV_MODE) }
        confirmVerified(featureManager)
    }
}