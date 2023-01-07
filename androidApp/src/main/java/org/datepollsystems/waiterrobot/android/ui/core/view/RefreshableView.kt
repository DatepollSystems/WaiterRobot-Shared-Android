package org.datepollsystems.waiterrobot.android.ui.core.view

import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * [content] must be vertical scrollable
 * - LazyColumn is default scrollable
 * - For other elements `Modifier.verticalScroll(rememberScrollState())` can be used
 *
 * Also make sure that the [content] fills the whole screen/view height
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RefreshableView(
    loading: Boolean,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(loading, onRefresh)

    Box(modifier.pullRefresh(pullRefreshState)) {
        content()

        PullRefreshIndicator(loading, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}
