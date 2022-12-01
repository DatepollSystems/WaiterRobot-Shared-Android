package org.datepollsystems.waiterrobot.shared.core.viewmodel

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.datepollsystems.waiterrobot.shared.core.di.injectLogger
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.message
import org.datepollsystems.waiterrobot.shared.generated.localization.title
import org.koin.core.component.KoinComponent
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import kotlin.reflect.KClass

// This flow is used to trigger a update of a ViewModel from an other ViewModel
private val updateViewModel: MutableSharedFlow<String> = MutableSharedFlow()

abstract class AbstractViewModel<S : ViewModelState, E : ViewModelEffect>(initialState: S) :
    ViewModel(), ContainerHost<S, E>, KoinComponent {

    protected val logger by injectLogger(this::class.simpleName!!)

    final override val container: Container<S, E> = viewModelScope.container(
        initialState = initialState,
        onCreate = ::onCreate,
        buildSettings = {
            exceptionHandler = CoroutineExceptionHandler { _, exception ->
                logger.w(exception) { "Unhandled exception in intent. Exceptions should be handled directly in the intent!" }

                intent { reduceError(L.app.genericError.title(), L.app.genericError.message()) }
            }
        }
    )

    init {
        viewModelScope.launch {
            updateViewModel.collect {
                if (it == this@AbstractViewModel::class.qualifiedName) {
                    logger.d { "Triggered update by other ViewModel" }
                    update()
                }
            }
        }
    }

    // Default implementation
    protected open fun onCreate(state: S): Unit = Unit

    protected suspend fun SimpleSyntax<S, E>.reduceError(
        errorTitle: String,
        errorMsg: String,
        dismiss: () -> Unit = this@AbstractViewModel::dismissError
    ) = reduce {
        @Suppress("UNCHECKED_CAST")
        // Swift does not support recursive Generics so we have to cast here
        state.withViewState(ViewState.Error(errorTitle, errorMsg, dismiss)) as S
    }

    protected fun dismissError() = intent {
        reduce {
            @Suppress("UNCHECKED_CAST")
            // Swift does not support recursive Generics so we have to cast here
            state.withViewState(ViewState.Idle) as S
        }
    }

    /** This function gets called when a sub view model changes something which must also change in this view model */
    protected open fun update() {
        logger.w("Received update request but there is no update implementation for the ViewModel")
    }

    /** With this function a update of an other ViewModel can be triggered */
    protected inline fun <reified T : AbstractViewModel<*, *>> updateParent(): Unit =
        updateParent(T::class)

    /** With this function a update of an other ViewModel can be triggered */
    protected fun updateParent(clazz: KClass<out AbstractViewModel<*, *>>) {
        if (this::class == clazz) {
            logger.w { "updateParent should only be used to update other ViewModels. Call update directly." }
        }

        val qualifiedName = clazz.qualifiedName
            ?: throw IllegalArgumentException("ViewModels must have a qualifiedName")

        viewModelScope.launch {
            updateViewModel.emit(qualifiedName)
        }
    }
}
