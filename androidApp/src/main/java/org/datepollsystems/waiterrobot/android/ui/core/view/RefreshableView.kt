package org.datepollsystems.waiterrobot.android.ui.core.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * [content] must be vertical scrollable
 * - LazyColumn is default scrollable
 * - For other elements `Modifier.verticalScroll(rememberScrollState())` can be used
 *
 * Also make sure that the [content] fills the whole screen/view height
 */
@Composable
fun RefreshableView(
    loading: Boolean,
    modifier: Modifier = Modifier,
    onRefresh: (() -> Unit),
    content: @Composable () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(loading),
        onRefresh = onRefresh,
        swipeEnabled = true,
        modifier = modifier.fillMaxWidth()
    ) {
        content()
    }
}
