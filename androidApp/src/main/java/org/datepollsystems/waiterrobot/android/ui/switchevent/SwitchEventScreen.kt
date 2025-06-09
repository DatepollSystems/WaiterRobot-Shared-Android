package org.datepollsystems.waiterrobot.android.ui.switchevent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import org.datepollsystems.waiterrobot.android.ui.core.ErrorBar
import org.datepollsystems.waiterrobot.android.ui.core.LocalSnackbarHostState
import org.datepollsystems.waiterrobot.android.ui.core.handleSideEffects
import org.datepollsystems.waiterrobot.android.ui.core.invoke
import org.datepollsystems.waiterrobot.android.ui.core.view.LoadingView
import org.datepollsystems.waiterrobot.android.ui.core.view.RefreshableView
import org.datepollsystems.waiterrobot.shared.core.data.Resource
import org.datepollsystems.waiterrobot.shared.features.switchevent.presentation.SwitchEventViewModel
import org.datepollsystems.waiterrobot.shared.localization.MR
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
@Destination<RootGraph>
fun SwitchEventScreen(
    navigator: NavController,
    vm: SwitchEventViewModel = koinViewModel()
) {
    val state by vm.collectAsState()

    vm.handleSideEffects(navigator)

    Scaffold(snackbarHost = { SnackbarHost(LocalSnackbarHostState.current) }) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Surface wrapper container is needed as otherwise the PullRefreshIndicator would be
            // on top of this part of the view
            Surface(modifier = Modifier.zIndex(1f)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Groups,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.3f)
                            .aspectRatio(1f)
                    )
                    Text(
                        text = MR.strings.switchEvent_desc(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(15.dp)
                    )
                }
            }

            HorizontalDivider(thickness = 2.dp)

            val eventResource = state.events
            RefreshableView(
                modifier = Modifier.weight(1f),
                loading = eventResource is Resource.Loading && eventResource.data != null,
                onRefresh = vm::loadEvents,
            ) {
                val events = eventResource.data
                if (eventResource is Resource.Loading && events == null) {
                    LoadingView()
                } else {
                    if (eventResource is Resource.Error) {
                        ErrorBar(message = eventResource.userMessage, retryAction = vm::loadEvents)
                    }
                    if (events.isNullOrEmpty()) {
                        Box(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState()) // Needed for Refreshable view
                        ) {
                            Text(
                                text = MR.strings.switchEvent_noEventFound(),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(events, key = { it.id }) { event ->
                                Box(
                                    modifier = Modifier
                                        .clickable { vm.onEventSelected(event) }
                                        .padding(horizontal = 20.dp, vertical = 10.dp)
                                ) {
                                    Event(event)
                                }
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }

            HorizontalDivider()
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TextButton(
                    onClick = vm::logout,
                ) {
                    Text(MR.strings.settings_general_logout_action())
                }
            }
        }
    }
}
