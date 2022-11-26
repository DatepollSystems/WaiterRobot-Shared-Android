package org.datepollsystems.waiterrobot.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.datepollsystems.waiterrobot.android.ui.core.theme.WaiterRobotTheme
import org.datepollsystems.waiterrobot.android.ui.tablelist.TableListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Switch from SplashScreenTheme to AppTheme
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContent {
            WaiterRobotTheme {
                TableListScreen()
            }
        }
    }
}
