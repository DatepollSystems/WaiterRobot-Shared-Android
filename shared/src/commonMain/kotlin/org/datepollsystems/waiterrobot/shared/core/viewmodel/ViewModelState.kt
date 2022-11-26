package org.datepollsystems.waiterrobot.shared.core.viewmodel

abstract class ViewModelState {
    abstract val viewState: ViewState

    internal abstract fun withViewState(viewState: ViewState): ViewModelState
}
