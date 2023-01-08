package org.datepollsystems.waiterrobot.android.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import org.datepollsystems.waiterrobot.android.ui.common.LinkText
import org.datepollsystems.waiterrobot.android.ui.common.SingleSelectDialog
import org.datepollsystems.waiterrobot.android.ui.core.handleNavAction
import org.datepollsystems.waiterrobot.android.ui.core.view.View
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.features.settings.models.AppTheme
import org.datepollsystems.waiterrobot.shared.features.settings.viewmodel.SettingsEffect
import org.datepollsystems.waiterrobot.shared.features.settings.viewmodel.SettingsViewModel
import org.datepollsystems.waiterrobot.shared.generated.localization.*
import org.koin.androidx.compose.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
@Destination
fun SettingsScreen(
    navigator: NavController,
    vm: SettingsViewModel = getViewModel()
) {
    val state = vm.collectAsState().value

    vm.collectSideEffect { handleSideEffect(it, navigator) }

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
            options = AppTheme.values().toList(),
            optionId = AppTheme::ordinal,
            optionText = { it.settingsText() },
            selected = state.currentAppTheme,
            onSelect = vm::switchTheme,
            onDismissRequest = { showThemeSelectDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = L.settings.title()) },
                navigationIcon = {
                    IconButton(onClick = { navigator.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showLogoutWarningDialog = true }) {
                        Icon(Icons.Filled.Logout, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { contentPadding ->
        View(
            modifier = Modifier.padding(contentPadding),
            state = state
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    settingsItem(
                        icon = { Icon(Icons.Filled.Logout, contentDescription = "Logout") },
                        title = { Text(L.settings.logout.action()) },
                        subtitle = { Text("\"${CommonApp.settings.organisationName}\" / \"${CommonApp.settings.waiterName}\"") },
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

                Divider(thickness = 2.dp)
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
}

private fun handleSideEffect(effect: SettingsEffect, navigator: NavController) {
    when (effect) {
        is SettingsEffect.Navigate -> navigator.handleNavAction(effect.action)
    }
}
