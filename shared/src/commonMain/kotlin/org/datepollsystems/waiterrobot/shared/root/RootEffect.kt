package org.datepollsystems.waiterrobot.shared.root

import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelEffect

sealed class RootEffect : ViewModelEffect {
    @Deprecated("SideEffects are considered an anti-pattern")
    data class ShowSnackBar(val message: String) : RootEffect()
}
