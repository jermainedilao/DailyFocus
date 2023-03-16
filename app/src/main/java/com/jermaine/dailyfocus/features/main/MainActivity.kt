package com.jermaine.dailyfocus.features.main

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
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jermaine.dailyfocus.features.home.HomeScreen
import com.jermaine.dailyfocus.features.onboarding.OnboardingScreen
import com.jermaine.dailyfocus.ui.theme.DailyFocusTheme
import com.jermaine.dailyfocus.util.NAVIGATION_HOME
import com.jermaine.dailyfocus.util.NAVIGATION_ONBOARDING
import com.jermaine.dailyfocus.util.PREF_IS_ONBOARDED
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
                        }
                    )
                }
            }
            composable(NAVIGATION_HOME) {
                HomeScreen()
            }
        }
    }
}