package org.datepollsystems.waiterrobot.android.ui.switchevent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import org.datepollsystems.waiterrobot.android.ui.core.handleNavAction
import org.datepollsystems.waiterrobot.android.ui.core.view.View
import org.datepollsystems.waiterrobot.shared.features.switchevent.viewmodel.SwitchEventEffect
import org.datepollsystems.waiterrobot.shared.features.switchevent.viewmodel.SwitchEventViewModel
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.desc
import org.datepollsystems.waiterrobot.shared.generated.localization.noEventFound
import org.koin.androidx.compose.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
@Destination
fun SwitchEventScreen(
    navigator: NavController,
    scaffoldState: ScaffoldState,
    vm: SwitchEventViewModel = getViewModel()
) {
    val state = vm.collectAsState().value

    vm.collectSideEffect { handleSideEffects(it, navigator) }

    Scaffold(scaffoldState = scaffoldState) {
        Column {
            Icon(
                imageVector = Icons.Outlined.Groups,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .aspectRatio(1f)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = L.switchEvent.desc(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )
            Divider()

            View(
                state = state,
                onRefresh = vm::loadEvents
            ) {
                if (state.events.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()) // Needed for Refreshable view
                    ) {
                        Text(
                            text = L.switchEvent.noEventFound(),
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
                        items(state.events) { event ->
                            Box(
                                modifier = Modifier
                                    .clickable { vm.onEventSelected(event) }
                                    .padding(horizontal = 20.dp, vertical = 10.dp)
                            ) {
                                Event(event)
                            }
                            Divider()
                        }
                    }
                }
            }
        }
    }
}

private fun handleSideEffects(effect: SwitchEventEffect, navigator: NavController) {
    when (effect) {
        is SwitchEventEffect.Navigation -> navigator.handleNavAction(effect.action)
    }
}
