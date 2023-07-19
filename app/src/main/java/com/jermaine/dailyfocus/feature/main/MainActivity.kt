package com.jermaine.dailyfocus.feature.main

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navOptions
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jermaine.dailyfocus.feature.addtask.AddTaskScreen
import com.jermaine.dailyfocus.feature.archives.ArchivesScreen
import com.jermaine.dailyfocus.feature.home.HomeScreen
import com.jermaine.dailyfocus.feature.onboarding.OnboardingScreen
import com.jermaine.dailyfocus.ui.theme.DailyFocusTheme
import com.jermaine.dailyfocus.util.ARGS_ID
import com.jermaine.dailyfocus.util.NAVIGATION_ADD_TASK
import com.jermaine.dailyfocus.util.NAVIGATION_ARCHIVES
import com.jermaine.dailyfocus.util.NAVIGATION_HOME
import com.jermaine.dailyfocus.util.NAVIGATION_ONBOARDING
import com.jermaine.dailyfocus.util.PREF_IS_ONBOARDED
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.util.UUID
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalPagerApi
@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DailyFocusTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    DailyFocus()
                }
            }
        }
    }

    @Composable
    private fun DailyFocus() {
        val navController = rememberNavController()
        val startDestination =
            if (sharedPreferences.getBoolean(PREF_IS_ONBOARDED, false)) {
                NAVIGATION_HOME
            } else {
                NAVIGATION_ONBOARDING
            }

        NavHost(navController = navController, startDestination = startDestination) {
            composable(NAVIGATION_ONBOARDING) {
                OnboardingScreen {
                    sharedPreferences.edit().putBoolean(PREF_IS_ONBOARDED, true).apply()

                    navController.navigate(
                        NAVIGATION_HOME,
                        navOptions {
                            popUpTo(NAVIGATION_ONBOARDING) {
                                inclusive = true
                            }
                        },
                    )
                }
            }
            composable(NAVIGATION_HOME) {
                HomeScreen(
                    onAddTaskClick = {
                        navController.navigate(
                            NAVIGATION_ADD_TASK,
                        )
                    },
                    onViewTaskClick = {
                        navController.navigate(
                            "$NAVIGATION_ADD_TASK?$ARGS_ID=$it",
                        )
                    },
                    onViewArchivesClick = {
                        navController.navigate(NAVIGATION_ARCHIVES)
                    },
                )
            }
            composable(NAVIGATION_ARCHIVES) {
                ArchivesScreen {
                    navController.navigateUp()
                }
            }
            composable(
                "$NAVIGATION_ADD_TASK?$ARGS_ID={$ARGS_ID}",
                arguments = listOf(
                    navArgument(ARGS_ID) {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                ),
            ) { backStackEntry ->
                AddTaskScreen(
                    id = backStackEntry.arguments?.getString(ARGS_ID)?.let {
                        UUID.fromString(it)
                    },
                    onAddTaskCompleteListener = {
                        navController.navigateUp()
                    },
                    onCloseClickListener = {
                        navController.navigateUp()
                    },
                )
            }
        }
    }
}
