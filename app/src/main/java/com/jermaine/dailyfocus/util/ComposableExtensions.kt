package com.jermaine.dailyfocus.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver

// Source: https://betterprogramming.pub/jetpack-compose-with-lifecycle-aware-composables-7bd5d6793e0
@Composable
fun <Observer : LifecycleObserver> Observer.observeLifecycle(lifecycle: Lifecycle) {
    DisposableEffect(key1 = lifecycle) {
        lifecycle.addObserver(this@observeLifecycle)
        onDispose {
            lifecycle.removeObserver(this@observeLifecycle)
        }
    }
}
