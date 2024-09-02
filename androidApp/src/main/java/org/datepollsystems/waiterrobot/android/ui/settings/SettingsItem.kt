package org.datepollsystems.waiterrobot.android.ui.settings

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.outlined.Contactless
import androidx.compose.material.icons.outlined.CurrencyExchange
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.datepollsystems.waiterrobot.android.ui.core.Preview
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.action
import org.datepollsystems.waiterrobot.shared.generated.localization.desc
import org.datepollsystems.waiterrobot.shared.generated.localization.privacyPolicy
import org.datepollsystems.waiterrobot.shared.generated.localization.title

fun LazyListScope.settingsItem(
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    title: String,
    subtitle: String? = null,
    action: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) = settingsItem(
    modifier = modifier,
    icon = icon,
    title = { Text(title) },
    subtitle = subtitle?.let { { Text(subtitle) } },
    action = action,
    onClick = onClick
)

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

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.settingsSection(
    text: String,
    modifier: Modifier = Modifier,
    items: LazyListScope.() -> Unit
) {
    stickyHeader {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary),
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp)
        )
    }
    items()
}

@Preview
@Composable
private fun SettingsPreview() = Preview {
    LazyColumn {
        settingsSection(L.settings.general.title()) {
            settingsItem(
                icon = { Icon(Icons.Filled.Logout, contentDescription = "Logout") },
                title = L.settings.general.logout.action(),
                subtitle = "\"My Org\" / \"My Event\"",
                onClick = { }
            )
            settingsItem(
                icon = { Icon(Icons.Outlined.Groups, contentDescription = "Switch event") },
                title = L.switchEvent.title(),
                subtitle = "My Event",
                onClick = {}
            )
            settingsItem(
                icon = {
                    Icon(Icons.Outlined.Refresh, contentDescription = "Refresh data")
                },
                title = L.settings.general.refresh.title(),
                subtitle = L.settings.general.refresh.desc(),
                onClick = {}
            )
        }

        settingsSection(L.settings.payment.title()) {
            settingsItem(
                icon = { Icon(Icons.Outlined.CurrencyExchange, contentDescription = null) },
                title = L.settings.payment.skipMoneyBackDialog.title(),
                subtitle = L.settings.payment.skipMoneyBackDialog.desc(),
                action = {
                    Switch(checked = true, onCheckedChange = null)
                },
                onClick = {}
            )
            settingsItem(
                icon = {
                    Icon(
                        Icons.Outlined.Contactless,
                        contentDescription = L.settings.payment.cardPayment.title()
                    )
                },
                title = L.settings.payment.cardPayment.title(),
                subtitle = L.settings.payment.cardPayment.desc(),
                onClick = { }
            )
        }

        settingsSection(L.settings.about.title()) {
            settingsItem(
                icon = { Icon(Icons.Filled.PrivacyTip, contentDescription = "Privacy") },
                title = L.settings.about.privacyPolicy(),
                onClick = { }
            )
            settingsItem(
                icon = { Icon(Icons.Filled.Info, contentDescription = "App info") },
                title = L.settings.about.version.title(),
                subtitle = "Version 9.9.9 (123456789)"
            )
        }
    }
}
