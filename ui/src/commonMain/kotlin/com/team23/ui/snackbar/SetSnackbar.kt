package com.team23.ui.snackbar

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SetSnackbar(
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarHostState = remember { SnackbarHostState() }
    val currentJob = remember { mutableStateOf<Job?>(null) }

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                SnackbarManager.snackbar.collect { snackbarData ->
                    snackbarHostState.currentSnackbarData?.dismiss()
                    currentJob.value?.cancel()
                    currentJob.value = coroutineScope.launch {
                        snackbarHostState.showSnackbar(snackbarData)
                    }
                }
            }
        }
    }

    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier,
    ) { data ->
        Snackbar(data)
    }
}
