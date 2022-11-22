package org.datepollsystems.waiterrobot.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import org.datepollsystems.waiterrobot.Greeting

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Switch from StartUpTheme to AppTheme
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContent {
            WaiterRobotTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Text(Greeting().greeting())
                }
            }
        }
    }
}
