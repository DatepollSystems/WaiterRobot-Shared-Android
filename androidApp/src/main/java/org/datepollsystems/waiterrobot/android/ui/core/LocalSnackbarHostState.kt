package org.datepollsystems.waiterrobot.android.ui.core

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf

val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> {
    error("No Composition local for LocalSnackbarHostState provided")
}
