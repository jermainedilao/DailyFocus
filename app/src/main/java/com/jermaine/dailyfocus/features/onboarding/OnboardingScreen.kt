package com.jermaine.dailyfocus.features.onboarding

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jermaine.dailyfocus.ui.theme.DailyFocusTheme
import com.jermaine.dailyfocus.ui.theme.grids

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun OnboardingScreen() {
    Scaffold(
        modifier = Modifier.padding(
            all = MaterialTheme.grids.grid16
        )
    ) {
        Text(text = "Hello onboarding!")
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DailyFocusTheme {
        OnboardingScreen()
    }
}