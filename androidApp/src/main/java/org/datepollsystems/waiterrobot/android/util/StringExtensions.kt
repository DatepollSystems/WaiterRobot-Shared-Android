package org.datepollsystems.waiterrobot.android.util

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import org.datepollsystems.waiterrobot.shared.utils.extensions.emptyToNull

fun String?.toColor(): Color? = try {
    this.emptyToNull()?.let { Color(it.toColorInt()) }
} catch (_: Exception) {
    null
}
