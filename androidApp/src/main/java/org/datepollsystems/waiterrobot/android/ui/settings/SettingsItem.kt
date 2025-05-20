package org.datepollsystems.waiterrobot.android.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.outlined.Contactless
import androidx.compose.material.icons.outlined.CurrencyExchange
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import org.datepollsystems.waiterrobot.android.ui.core.invoke
import org.datepollsystems.waiterrobot.android.ui.core.preview.Preview
import org.datepollsystems.waiterrobot.shared.localization.MR

fun LazyListScope.settingsItem(
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    title: StringDesc,
    subtitle: StringDesc? = null,
    action: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) = settingsItem(
    modifier = modifier,
    icon = icon,
    title = { Text(title()) },
    subtitle = subtitle?.let { { Text(subtitle()) } },
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
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .let {
                if (onClick != null) {
                    it.clickable(onClick = onClick)
                } else {
                    it
                }
            }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    icon()
                    Spacer(modifier = Modifier.width(12.dp))
                }
                SettingsTitle(title = title, subtitle = subtitle)
            }
            if (action != null) {
                Spacer(modifier = Modifier.width(12.dp))
                action()
            }
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

fun LazyListScope.settingsSection(
    text: StringDesc,
    modifier: Modifier = Modifier,
    items: LazyListScope.() -> Unit
) {
    stickyHeader {
        Text(
            text = text(),
            style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary),
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 12.dp, top = 16.dp)
        )
    }
    items()
}

@Preview
@Composable
private fun SettingsPreview() = Preview {
    var selected by remember { mutableStateOf(false) }
    LazyColumn {
        settingsSection(MR.strings.settings_general_title.desc()) {
            settingsItem(
                icon = {
                    Icon(
                        Icons.Filled.Logout,
                        contentDescription = MR.strings.settings_general_logout_action()
                    )
                },
                title = MR.strings.settings_general_logout_action.desc(),
                subtitle = "\"My Org\" / \"My Event\"".desc(),
                onClick = { }
            )
            settingsItem(
                icon = {
                    Icon(
                        Icons.Outlined.Groups,
                        contentDescription = MR.strings.switchEvent_title()
                    )
                },
                title = MR.strings.switchEvent_title.desc(),
                subtitle = "My Event".desc(),
                onClick = {}
            )
            settingsItem(
                title = MR.strings.settings_general_refresh_title.desc(),
                subtitle = MR.strings.settings_general_refresh_desc.desc(),
                onClick = {}
            )
        }

        settingsSection(MR.strings.settings_payment_title.desc()) {
            settingsItem(
                icon = { Icon(Icons.Outlined.CurrencyExchange, contentDescription = null) },
                title = MR.strings.settings_payment_skipMoneyBackDialog_title.desc(),
                subtitle = MR.strings.settings_payment_skipMoneyBackDialog_desc.desc(),
                action = {
                    Switch(checked = selected, onCheckedChange = { selected = !selected })
                },
                onClick = { selected = !selected }
            )
            settingsItem(
                icon = {
                    Icon(
                        Icons.Outlined.Contactless,
                        contentDescription = MR.strings.settings_payment_card_title()
                    )
                },
                title = MR.strings.settings_payment_card_title.desc(),
                subtitle = MR.strings.settings_payment_card_desc.desc(),
                onClick = { }
            )
        }

        settingsSection(MR.strings.settings_about_title.desc()) {
            settingsItem(
                icon = {
                    Icon(
                        Icons.Filled.PrivacyTip,
                        contentDescription = MR.strings.settings_about_privacyPolicy()
                    )
                },
                title = MR.strings.settings_about_privacyPolicy.desc(),
                onClick = { }
            )
            settingsItem(
                icon = { Icon(Icons.Filled.Info, contentDescription = "App info") },
                title = MR.strings.settings_about_version_title.desc(),
                subtitle = "Version 9.9.9 (123456789)".desc()
            )
        }
    }
}
