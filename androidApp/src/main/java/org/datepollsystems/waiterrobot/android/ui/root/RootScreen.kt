package org.datepollsystems.waiterrobot.android.ui.root

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import org.datepollsystems.waiterrobot.android.ui.core.view.View
import org.datepollsystems.waiterrobot.android.ui.login.LoginScreen
import org.datepollsystems.waiterrobot.android.ui.tablelist.TableListScreen
import org.datepollsystems.waiterrobot.shared.root.RootViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
@Destination
@RootNavGraph(start = true)
fun RootScreen(vm: RootViewModel, scaffoldState: ScaffoldState, navigator: NavController) {
    val state = vm.collectAsState().value

    View(state = state) {
        when {
            !state.isLoggedIn -> LoginScreen(scaffoldState = scaffoldState, navigator = navigator)
            else -> TableListScreen(scaffoldState = scaffoldState, navigator = navigator)
        }
    }
}
