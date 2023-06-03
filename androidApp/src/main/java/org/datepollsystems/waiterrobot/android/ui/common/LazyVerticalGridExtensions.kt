package org.datepollsystems.waiterrobot.android.ui.common

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable

fun LazyGridScope.header(
    key: Any? = null,
    contentType: Any? = null,
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(
        key = key,
        contentType = contentType,
        span = { GridItemSpan(this.maxLineSpan) },
        content = content
    )
}
