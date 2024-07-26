@AndroidEntryPoint(BaseActivity::class)
class TrackerMyHeartOnboardingActivity : Hilt_TrackerMyHeartOnboardingActivity() {

    companion object {
        const val INPUT_EXTRA_SKIP_TO_LAST_SCREEN = "skip_to_last_screen"

        fun startActivity(context: Context, skipToLastScreen: Boolean) {
            val intent = Intent(context, TrackerMyHeartOnboardingActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra(INPUT_EXTRA_SKIP_TO_LAST_SCREEN, skipToLastScreen)
            }
            context.startActivity(intent)
        }
    }

    private val viewModel: OnboardingViewModel by viewModels()

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.MyHeartThemeLightNoBar)
        super.onCreate(savedInstanceState)
        if (shouldStop()) {
            return
        }

        setContentView(R.layout.tracker_my_heart_onboarding_activity)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        if (intent.getBooleanExtra(INPUT_EXTRA_SKIP_TO_LAST_SCREEN, false)) {
            viewModel.setSkipToLastScreen()
            navigateToNextPage()
        }
    }

    override fun getServiceId(): String {
        return ServiceId.TRACKER_MY_HEART
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToNextPage() {
        if (navController?.currentDestination?.id == R.id.trackerIntroFragment) {
            navController?.navigate(R.id.onboardConfirmFragment)
        }
    }
}
