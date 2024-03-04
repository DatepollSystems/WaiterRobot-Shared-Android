package org.datepollsystems.waiterrobot.android.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import org.datepollsystems.waiterrobot.android.ui.common.LinkText
import org.datepollsystems.waiterrobot.android.ui.common.SingleSelectDialog
import org.datepollsystems.waiterrobot.android.ui.core.handleSideEffects
import org.datepollsystems.waiterrobot.android.ui.core.view.ScaffoldView
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.features.settings.models.AppTheme
import org.datepollsystems.waiterrobot.shared.features.settings.viewmodel.SettingsViewModel
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.action
import org.datepollsystems.waiterrobot.shared.generated.localization.desc
import org.datepollsystems.waiterrobot.shared.generated.localization.keepLoggedIn
import org.datepollsystems.waiterrobot.shared.generated.localization.privacyPolicy
import org.datepollsystems.waiterrobot.shared.generated.localization.title
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
@Destination
fun SettingsScreen(
    navigator: NavController,
    vm: SettingsViewModel = koinViewModel()
) {
    val state by vm.collectAsState()

    vm.handleSideEffects(navigator)

    var showLogoutWarningDialog by remember { mutableStateOf(false) }
    if (showLogoutWarningDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutWarningDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutWarningDialog = false
                    vm.logout()
                }) {
                    Text(L.settings.logout.action())
                }
            },
            dismissButton = {
                Button(onClick = { showLogoutWarningDialog = false }) {
                    Text(L.settings.keepLoggedIn())
                }
            },
            title = {
                Text(
                    text = L.settings.logout.title(CommonApp.settings.organisationName)
                )
            },
            text = {
                Text(
                    text = L.settings.logout.desc(CommonApp.settings.organisationName),
                    textAlign = TextAlign.Center
                )
            }
        )
    }

    var showThemeSelectDialog by remember { mutableStateOf(false) }
    if (showThemeSelectDialog) {
        SingleSelectDialog(
            title = L.settings.darkMode.title(),
            options = AppTheme.entries,
            optionId = AppTheme::ordinal,
            optionText = { it.settingsText() },
            selected = state.currentAppTheme,
            onSelect = vm::switchTheme,
            onDismissRequest = { showThemeSelectDialog = false }
        )
    }

    ScaffoldView(
        state = state,
        title = L.settings.title(),
        topBarActions = {
            IconButton(onClick = { showLogoutWarningDialog = true }) {
                Icon(Icons.Filled.Logout, contentDescription = "Logout")
            }
        },
        navigationIcon = {
            IconButton(onClick = { navigator.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        },
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                settingsItem(
                    icon = { Icon(Icons.Filled.Logout, contentDescription = "Logout") },
                    title = { Text(L.settings.logout.action()) },
                    subtitle = {
                        Text(
                            "\"${CommonApp.settings.organisationName}\" / \"${CommonApp.settings.waiterName}\""
                        )
                    },
                    onClick = { showLogoutWarningDialog = true }
                )
                settingsItem(
                    icon = { Icon(Icons.Outlined.Groups, contentDescription = "Switch event") },
                    title = { Text(L.switchEvent.title()) },
                    subtitle = { Text(CommonApp.settings.eventName) },
                    onClick = vm::switchEvent
                )
                settingsItem(
                    icon = { Icon(Icons.Outlined.DarkMode, contentDescription = "Use dark") },
                    title = { Text(L.settings.darkMode.title()) },
                    subtitle = { Text(state.currentAppTheme.settingsText()) },
                    onClick = { showThemeSelectDialog = true }
                )
                settingsItem(
                    icon = {
                        Icon(
                            Icons.Outlined.Refresh,
                            contentDescription = "Refresh data"
                        )
                    },
                    title = { Text(L.settings.refresh.title()) },
                    subtitle = { Text(L.settings.refresh.desc()) },
                    onClick = {
                        vm.refreshAll()
                    }
                )

                settingsItem(
                    icon = { Icon(Icons.Filled.Info, contentDescription = "App info") },
                    title = { Text(L.settings.version.title()) },
                    subtitle = { Text(state.versionString) }
                )
            }

            HorizontalDivider(thickness = 2.dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                LinkText(
                    text = L.settings.privacyPolicy(),
                    url = CommonApp.privacyPolicyUrl
                )
            }
        }
    }
}
