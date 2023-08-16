package org.datepollsystems.waiterrobot.android.ui.core.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.datepollsystems.waiterrobot.android.ui.core.LocalScaffoldState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState

/**
 * Handles displaying errors and loading state.
 * If [onRefresh] is provided a [RefreshableView] is used and [content] therefore
 * must be scrollable ([RefreshableView]). Otherwise a [LoadableView] is used.
 * @see ErrorDialog
 * @see RefreshableView
 * @see LoadableView
 */
@Composable
fun View(
    state: ViewModelState,
    modifier: Modifier = Modifier,
    onRefresh: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val viewState = state.viewState
    if (onRefresh != null) {
        RefreshableView(
            modifier = modifier,
            loading = viewState == ViewState.Loading,
            onRefresh = onRefresh,
            content = content
        )
    } else {
        LoadableView(
            modifier = modifier,
            loading = viewState == ViewState.Loading,
            content = content
        )
    }

    if (viewState is ViewState.Error) {
        ErrorDialog(viewState)
    }
}

/**
 * see [View]
 */
@Composable
fun View(
    state: ViewModelState,
    paddingValues: PaddingValues,
    onRefresh: (() -> Unit)? = null,
    content: @Composable () -> Unit
) = View(state, Modifier.padding(paddingValues), onRefresh, content)

@Composable
fun ScaffoldView(
    state: ViewModelState,
    title: String,
    topBarActions: @Composable RowScope.() -> Unit = {},
    navigationIcon: @Composable (() -> Unit)? = null,
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    isFloatingActionButtonDocked: Boolean = false,
    onRefresh: (() -> Unit)? = null,
    content: @Composable () -> Unit
) = Scaffold(
    scaffoldState = LocalScaffoldState.current,
    topBar = {
        TopAppBar(
            title = { Text(title) },
            actions = topBarActions,
            navigationIcon = navigationIcon
        )
    },
    bottomBar = bottomBar,
    floatingActionButton = floatingActionButton,
    floatingActionButtonPosition = floatingActionButtonPosition,
    isFloatingActionButtonDocked = isFloatingActionButtonDocked
) {
    View(state = state, paddingValues = it, onRefresh = onRefresh, content = content)
}
