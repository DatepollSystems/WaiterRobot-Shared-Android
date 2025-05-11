package org.datepollsystems.waiterrobot.shared.root.presentation

import dev.icerock.moko.resources.desc.StringDesc
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelEffect

sealed class RootEffect : ViewModelEffect {
    @Deprecated("SideEffects are considered an anti-pattern")
    data class ShowSnackBar(val message: StringDesc) : RootEffect()
}
