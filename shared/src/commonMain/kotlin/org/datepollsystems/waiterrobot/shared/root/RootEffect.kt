package org.datepollsystems.waiterrobot.shared.root

import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelEffect

sealed class RootEffect : ViewModelEffect {
    data class ShowSnackBar(val message: String) : RootEffect()
}
