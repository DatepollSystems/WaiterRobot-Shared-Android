package org.datepollsystems.waiterrobot.shared.core.viewmodel

abstract class ViewModelState {
    @Deprecated("Legacy - Not used anymore")
    abstract val viewState: ViewState

    @Deprecated("Legacy - Not used anymore")
    internal abstract fun withViewState(viewState: ViewState): ViewModelState
}
