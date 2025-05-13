package org.datepollsystems.waiterrobot.android.ui.settings

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.outlined.Contactless
import androidx.compose.material.icons.outlined.CurrencyExchange
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.SelectAll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import dev.icerock.moko.resources.desc.desc
import org.datepollsystems.waiterrobot.android.ui.common.SingleSelectDialog
import org.datepollsystems.waiterrobot.android.ui.core.handleSideEffects
import org.datepollsystems.waiterrobot.android.ui.core.invoke
import org.datepollsystems.waiterrobot.android.ui.core.toast
import org.datepollsystems.waiterrobot.android.ui.core.view.ScaffoldView
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.features.settings.models.AppTheme
import org.datepollsystems.waiterrobot.shared.features.settings.viewmodel.SettingsEffect
import org.datepollsystems.waiterrobot.shared.features.settings.viewmodel.SettingsViewModel
import org.datepollsystems.waiterrobot.shared.features.switchevent.domain.model.Event
import org.datepollsystems.waiterrobot.shared.localization.MR
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
@Destination<RootGraph>
fun SettingsScreen(
    navigator: NavController,
    vm: SettingsViewModel = koinViewModel()
) {
    val state by vm.collectAsState()
    val selectedEvent by CommonApp.selectedEvent.collectAsState()

    var showLogoutWarningDialog by remember { mutableStateOf(false) }
    if (showLogoutWarningDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutWarningDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutWarningDialog = false
                    vm.logout()
                }) {
                    Text(MR.strings.settings_general_logout_action())
                }
            },
            dismissButton = {
                Button(onClick = { showLogoutWarningDialog = false }) {
                    Text(MR.strings.settings_general_logout_cancel())
                }
            },
            title = {
                Text(
                    text = MR.strings.settings_general_logout_title(CommonApp.settings.organisationName)
                )
            },
            text = {
                Text(
                    text = MR.strings.settings_general_logout_desc(CommonApp.settings.organisationName),
                    textAlign = TextAlign.Center
                )
            }
        )
    }

    var showThemeSelectDialog by remember { mutableStateOf(false) }
    if (showThemeSelectDialog) {
        SingleSelectDialog(
            title = MR.strings.settings_general_darkMode_title.desc(),
            options = AppTheme.entries,
            optionId = AppTheme::ordinal,
            optionText = { it.settingsText() },
            selected = state.currentAppTheme,
            onSelect = vm::switchTheme,
            onDismissRequest = { showThemeSelectDialog = false }
        )
    }

    var showConfirmSkipMoneyBackDialog by remember { mutableStateOf(false) }
    if (showConfirmSkipMoneyBackDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmSkipMoneyBackDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmSkipMoneyBackDialog = false
                    vm.toggleSkipMoneyBackDialog(value = true, confirmed = true)
                }) {
                    Text(MR.strings.settings_payment_skipMoneyBackDialog_confirm_action())
                }
            },
            dismissButton = {
                Button(onClick = { showConfirmSkipMoneyBackDialog = false }) {
                    Text(MR.strings.dialog_cancel())
                }
            },
            title = { Text(MR.strings.settings_payment_skipMoneyBackDialog_title()) },
            text = { Text(MR.strings.settings_payment_skipMoneyBackDialog_confirm_desc()) }
        )
    }

    vm.handleSideEffects(navigator) {
        when (it) {
            SettingsEffect.ConfirmSkipMoneyBackDialog -> showConfirmSkipMoneyBackDialog = true
        }
    }

    ScaffoldView(
        title = MR.strings.settings_title(),
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
    ) { padding ->
        val uriHandler = LocalUriHandler.current
        val context = LocalContext.current

        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {
            settingsSection(MR.strings.settings_general_title.desc()) {
                settingsItem(
                    icon = {
                        Icon(
                            Icons.Filled.Logout,
                            contentDescription = MR.strings.settings_general_logout_action()
                        )
                    },
                    title = MR.strings.settings_general_logout_action.desc(),
                    subtitle = with(CommonApp.settings) { "\"$organisationName\" / \"$waiterName\"".desc() },
                    onClick = { showLogoutWarningDialog = true }
                )
                settingsItem(
                    icon = {
                        Icon(
                            Icons.Outlined.Groups,
                            contentDescription = MR.strings.switchEvent_title()
                        )
                    },
                    title = MR.strings.switchEvent_title.desc(),
                    subtitle = CommonApp.settings.eventName.desc(),
                    onClick = vm::switchEvent
                )
                settingsItem(
                    icon = {
                        Icon(
                            Icons.Outlined.DarkMode,
                            contentDescription = MR.strings.settings_general_darkMode_title()
                        )
                    },
                    title = MR.strings.settings_general_darkMode_title.desc(),
                    subtitle = state.currentAppTheme.settingsText(),
                    onClick = { showThemeSelectDialog = true }
                )
                settingsItem(
                    icon = {
                        Icon(
                            Icons.Outlined.Refresh,
                            contentDescription = "Refresh data"
                        )
                    },
                    title = MR.strings.settings_general_refresh_title.desc(),
                    subtitle = MR.strings.settings_general_refresh_desc.desc(),
                    onClick = {
                        context.toast(
                            MR.strings.settings_general_refresh_toast.desc(),
                            Toast.LENGTH_SHORT
                        )
                        vm.refreshAll()
                    }
                )
            }

            settingsSection(MR.strings.settings_payment_title.desc()) {
                settingsItem(
                    icon = { Icon(Icons.Outlined.CurrencyExchange, contentDescription = null) },
                    title = MR.strings.settings_payment_skipMoneyBackDialog_title.desc(),
                    subtitle = MR.strings.settings_payment_skipMoneyBackDialog_desc.desc(),
                    action = {
                        Switch(
                            checked = state.skipMoneyBackDialog,
                            onCheckedChange = vm::toggleSkipMoneyBackDialog
                        )
                    },
                    onClick = vm::toggleSkipMoneyBackDialog
                )
                settingsItem(
                    icon = { Icon(Icons.Outlined.SelectAll, contentDescription = null) },
                    title = MR.strings.settings_payment_selectAllProductsByDefault_title.desc(),
                    subtitle = MR.strings.settings_payment_selectAllProductsByDefault_desc.desc(),
                    action = {
                        Switch(
                            checked = state.paymentSelectAllProductsByDefault,
                            onCheckedChange = vm::togglePaymentSelectAllProductsByDefault
                        )
                    },
                    onClick = vm::togglePaymentSelectAllProductsByDefault
                )
                if (selectedEvent?.stripeSettings is Event.StripeSettings.Enabled) {
                    settingsItem(
                        icon = {
                            Icon(
                                Icons.Outlined.Contactless,
                                contentDescription = MR.strings.settings_payment_card_title()
                            )
                        },
                        title = MR.strings.settings_payment_card_title.desc(),
                        subtitle = MR.strings.settings_payment_card_desc.desc(),
                        onClick = vm::initializeContactlessPayment
                    )
                }
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
                    onClick = { uriHandler.openUri(CommonApp.privacyPolicyUrl) }
                )
                settingsItem(
                    icon = { Icon(Icons.Filled.Info, contentDescription = "App info") },
                    title = MR.strings.settings_about_version_title.desc(),
                    subtitle = state.versionString
                )
            }
        }
    }
}
