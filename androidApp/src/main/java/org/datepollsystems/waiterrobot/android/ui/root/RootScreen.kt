package org.datepollsystems.waiterrobot.android.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import org.datepollsystems.waiterrobot.android.ui.core.view.View
import org.datepollsystems.waiterrobot.android.ui.login.LoginScreen
import org.datepollsystems.waiterrobot.android.ui.stripe.StripeInitializationScreen
import org.datepollsystems.waiterrobot.android.ui.switchevent.SwitchEventScreen
import org.datepollsystems.waiterrobot.android.ui.tablelist.TableListScreen
import org.datepollsystems.waiterrobot.shared.root.RootViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
@Destination
@RootNavGraph(start = true)
fun RootScreen(vm: RootViewModel, navigator: NavController) {
    val state by vm.collectAsState()

    View(state = state) {
        when {
            !state.isLoggedIn -> LoginScreen(navigator = navigator)
            !state.hasEventSelected -> SwitchEventScreen(navigator = navigator)
            state.needsStripeInitialization -> StripeInitializationScreen(navigator = navigator)
            else -> TableListScreen(navigator = navigator)
        }
    }
}
