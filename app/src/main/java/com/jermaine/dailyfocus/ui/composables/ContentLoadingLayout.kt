package com.jermaine.dailyfocus.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Inspired by View-based [ContentLoadingProgressBar](https://developer.android.com/reference/androidx/core/widget/ContentLoadingProgressBar).
 *
 * Waits `minDelayDuration` until contents of `loading` composable is shown when `isLoading` is
 * true. Once loading is shown and `isLoading` becomes `false`, waits `minShowDuration` until
 * content is shown to avoid flashes in the UI. If `isLoading` becomes `false` before
 * `minDelayDuration` passes, the loading state is not shown at all.
 *
 * [Source](https://gist.github.com/svenjacobs/cdcdd61998e6359b3fc0b4517536879b)
 *
 * @param isLoading Whether loading or content state should be shown
 * @param minDelayDuration Minimum delay until loading state is shown
 * @param minShowDuration Minimum delay until loading state is dismissed if active
 */
@Composable
fun ContentLinearProgressIndicator(
    isLoading: Boolean,
    minDelayDuration: Duration = 500.milliseconds,
    minShowDuration: Duration = 500.milliseconds,
) {
    var showLoading by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(isLoading) {
        if (isLoading && !showLoading) {
            delay(minDelayDuration)
            showLoading = true
        } else if (!isLoading && showLoading) {
            delay(minShowDuration)
            showLoading = false
        }
    }

    if (showLoading) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }
}