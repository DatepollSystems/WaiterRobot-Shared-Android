package org.datepollsystems.waiterrobot.shared.core.viewmodel

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.datepollsystems.waiterrobot.shared.core.data.api.ApiException
import org.datepollsystems.waiterrobot.shared.core.di.injectLoggerForClass
import org.datepollsystems.waiterrobot.shared.core.navigation.NavAction
import org.datepollsystems.waiterrobot.shared.core.navigation.NavOrViewModelEffect
import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.title
import org.datepollsystems.waiterrobot.shared.utils.getLocalizedUserMessage
import org.koin.core.component.KoinComponent
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitDsl
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import kotlin.reflect.KClass

// This flow is used to trigger a update of a ViewModel from an other ViewModel
@Deprecated("Replace by new architecture")
private val updateViewModel: MutableSharedFlow<String> = MutableSharedFlow()

typealias IntentContext<S, E> = SimpleSyntax<S, NavOrViewModelEffect<E>>

abstract class AbstractViewModel<S : ViewModelState, E : ViewModelEffect>(
    initialState: S
) : ViewModel(), ContainerHost<S, NavOrViewModelEffect<E>>, KoinComponent {

    protected val logger by injectLoggerForClass()

    final override val container: Container<S, NavOrViewModelEffect<E>> = viewModelScope.container(
        initialState = initialState,
        onCreate = {
            logger.d { "Creating Orbit container" }
            this.onCreate()
        },
        buildSettings = {
            exceptionHandler = CoroutineExceptionHandler { _, exception ->
                when (exception) {
                    is CancellationException -> Unit // Expected, nothing to do here

                    is ApiException.AppVersionTooOld -> intent {
                        navigator.replaceRoot(Screen.UpdateApp)
                    }

                    else -> {
                        logger.w(exception) {
                            "Unhandled exception in intent. " +
                                "Exceptions should be handled directly in the intent!"
                        }
                        intent {
                            reduceError(L.exceptions.title(), exception.getLocalizedUserMessage())
                        }
                    }
                }
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

    protected open suspend fun SimpleSyntax<S, NavOrViewModelEffect<E>>.onCreate(): Unit = Unit

    @Deprecated("Replace by new architecture")
    protected suspend fun SimpleSyntax<S, NavOrViewModelEffect<E>>.reduceError(
        errorTitle: String,
        errorMsg: String,
        dismiss: () -> Unit = this@AbstractViewModel::dismissError
    ) = reduce {
        @Suppress("UNCHECKED_CAST")
        // Swift does not support recursive Generics so we have to cast here
        state.withViewState(ViewState.Error(errorTitle, errorMsg, dismiss)) as S
    }

    @OrbitDsl
    protected val SimpleSyntax<S, NavOrViewModelEffect<E>>.navigator get() = Navigator(simpleSyntax = this)

    @OrbitDsl
    protected suspend fun SimpleSyntax<S, NavOrViewModelEffect<E>>.postSideEffect(effect: E) {
        postSideEffect(NavOrViewModelEffect.VMEffect(effect))
    }

    @Deprecated("Replace by new architecture")
    protected fun dismissError() {
        intent {
            reduce {
                @Suppress("UNCHECKED_CAST")
                // Swift does not support recursive Generics so we have to cast here
                state.withViewState(ViewState.Idle) as S
            }
        }
    }

    /** This function gets called when a sub view model changes something which must also change in this view model */
    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("Replace by new architecture")
    protected open fun update() {
        logger.w("Received update request but there is no update implementation for the ViewModel")
    }

    /** With this function a update of an other ViewModel can be triggered */
    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("Replace by new architecture")
    protected inline fun <reified T : AbstractViewModel<*, *>> updateParent(): Unit =
        updateParent(T::class)

    /** With this function a update of an other ViewModel can be triggered */
    @Deprecated("Replace by new architecture")
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

    inner class Navigator(private val simpleSyntax: SimpleSyntax<S, NavOrViewModelEffect<E>>) {
        @OrbitDsl
        suspend fun pop() = navigate(NavAction.Pop)

        @OrbitDsl
        suspend fun push(screen: Screen) = navigate(NavAction.Push(screen))

        @OrbitDsl
        suspend fun popUpTo(screen: Screen, inclusive: Boolean) =
            navigate(NavAction.PopUpTo(screen, inclusive))

        @OrbitDsl
        suspend fun popUpAndPush(screen: Screen, popUpTo: Screen, inclusive: Boolean) =
            navigate(NavAction.PopUpAndPush(screen, popUpTo, inclusive))

        @OrbitDsl
        suspend fun replaceRoot(screen: Screen) = navigate(NavAction.ReplaceRoot(screen))

        private suspend inline fun navigate(action: NavAction) =
            simpleSyntax.postSideEffect(NavOrViewModelEffect.NavEffect(action))
    }
}
