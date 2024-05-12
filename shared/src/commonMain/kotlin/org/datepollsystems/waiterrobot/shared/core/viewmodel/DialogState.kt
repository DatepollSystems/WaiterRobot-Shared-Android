package org.datepollsystems.waiterrobot.shared.core.viewmodel

data class DialogState(
    val title: String,
    val text: String,
    val onDismiss: () -> Unit,
    val primaryButton: Button,
    val secondaryButton: Button? = null,
) {
    data class Button(
        val text: String,
        val action: () -> Unit
    )

    enum class Type {
        ERROR
    }
}
