package org.datepollsystems.waiterrobot.android.ui.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CenteredText(text: String, scrollAble: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .let {
                if (scrollAble) {
                    it.verticalScroll(rememberScrollState())
                } else {
                    it
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center
        )
    }
}
