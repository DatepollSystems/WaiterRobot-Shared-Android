package org.datepollsystems.waiterrobot.shared.core.viewmodel

import dev.icerock.moko.resources.desc.StringDesc
import org.datepollsystems.waiterrobot.shared.core.viewmodel.DialogState.Button

sealed class ViewState {
    data object Idle : ViewState()
    data object Loading : ViewState()
    data class Error(val dialog: DialogState) : ViewState() {
        constructor(
            title: StringDesc,
            text: StringDesc,
            onDismiss: () -> Unit,
            primaryButton: Button,
            secondaryButton: Button? = null,
        ) : this(DialogState(title, text, onDismiss, primaryButton, secondaryButton))
    }
}
