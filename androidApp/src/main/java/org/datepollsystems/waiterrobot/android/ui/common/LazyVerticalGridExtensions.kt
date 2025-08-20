package org.datepollsystems.waiterrobot.android.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun LazyGridScope.sectionHeader(
    key: Any? = null,
    contentType: Any? = null,
    title: String
) {
    stickyHeader(key = key, contentType = contentType) {
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                text = title,
                modifier = Modifier.padding(horizontal = 10.dp),
                maxLines = 1,
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }
    }
}
