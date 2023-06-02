package org.datepollsystems.waiterrobot.android.ui.core

import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf

val LocalScaffoldState = compositionLocalOf {
    ScaffoldState(
        drawerState = DrawerState(DrawerValue.Closed),
        snackbarHostState = SnackbarHostState()
    )
}
