package org.datepollsystems.waiterrobot.shared.core.viewmodel

import dev.icerock.moko.resources.desc.StringDesc

data class DialogState(
    val title: StringDesc,
    val text: StringDesc,
    val onDismiss: () -> Unit,
    val primaryButton: Button,
    val secondaryButton: Button? = null,
) {
    data class Button(
        val text: StringDesc,
        val action: () -> Unit
    )

    enum class Type {
        ERROR
    }
}
