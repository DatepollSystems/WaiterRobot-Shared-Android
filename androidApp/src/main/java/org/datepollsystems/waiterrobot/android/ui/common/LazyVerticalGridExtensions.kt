package org.datepollsystems.waiterrobot.android.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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

fun LazyGridScope.sectionHeader(
    key: Any? = null,
    contentType: Any? = null,
    title: String
) {
    header(key = key, contentType = contentType) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(modifier = Modifier.weight(1f))
            Text(
                text = title,
                modifier = Modifier.padding(horizontal = 10.dp),
                maxLines = 1,
            )
            Divider(modifier = Modifier.weight(1f))
        }
    }
}
