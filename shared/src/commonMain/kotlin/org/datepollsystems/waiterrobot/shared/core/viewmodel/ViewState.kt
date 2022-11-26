package org.datepollsystems.waiterrobot.shared.core.viewmodel

sealed class ViewState {
    object Idle : ViewState()
    object Loading : ViewState()
    data class Error(val title: String, val message: String, val onDismiss: () -> Unit) :
        ViewState()
}
