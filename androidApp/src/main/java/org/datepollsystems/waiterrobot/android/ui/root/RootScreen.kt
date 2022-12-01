package org.datepollsystems.waiterrobot.android.ui.root

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import org.datepollsystems.waiterrobot.android.ui.tablelist.TableListScreen
import org.datepollsystems.waiterrobot.shared.root.RootViewModel

@Composable
@Destination
@RootNavGraph(start = true)
fun RootScreen(vm: RootViewModel, navigator: NavController) {
    TableListScreen(navigator = navigator)
}
