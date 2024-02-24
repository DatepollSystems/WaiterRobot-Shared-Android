package org.datepollsystems.waiterrobot.android.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun LazyListScope.settingsItem(
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    title: @Composable () -> Unit,
    subtitle: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) = item {
    SettingsItem(
        modifier = modifier,
        icon = icon,
        title = title,
        subtitle = subtitle,
        action = action,
        onClick = onClick
    )
    HorizontalDivider()
}

@Composable
private fun SettingsItem(
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)?,
    title: @Composable () -> Unit,
    subtitle: (@Composable () -> Unit)?,
    action: (@Composable () -> Unit)?,
    onClick: (() -> Unit)?
) {
    Surface {
        Row(
            modifier = modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .let {
                        if (onClick != null) {
                            it.clickable(onClick = onClick)
                        } else {
                            it
                        }
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                SettingsIcon(icon = icon)
                SettingsTitle(title = title, subtitle = subtitle)
            }
            if (action != null) {
                Divider(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .height(56.dp)
                        .width(1.dp)
                )
                Box(
                    modifier = Modifier.size(64.dp),
                    contentAlignment = Alignment.Center
                ) {
                    action()
                }
            }
        }
    }
}

@Composable
private fun SettingsIcon(
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null
) {
    Box(
        modifier = modifier.size(64.dp),
        contentAlignment = Alignment.Center
    ) {
        if (icon != null) {
            icon()
        }
    }
}

@Composable
private fun RowScope.SettingsTitle(
    title: @Composable () -> Unit,
    subtitle: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.Center
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.titleMedium) {
            title()
        }
        if (subtitle != null) {
            Spacer(modifier = Modifier.size(2.dp))
            ProvideTextStyle(value = MaterialTheme.typography.bodySmall) {
                subtitle()
            }
        }
    }
}
