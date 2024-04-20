package org.datepollsystems.waiterrobot.android.stripe

import android.annotation.SuppressLint
import com.stripe.stripeterminal.Terminal
import com.stripe.stripeterminal.external.callable.Callback
import com.stripe.stripeterminal.external.callable.DiscoveryListener
import com.stripe.stripeterminal.external.models.DiscoveryConfiguration
import com.stripe.stripeterminal.external.models.Reader
import com.stripe.stripeterminal.external.models.TerminalException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.runBlocking
import org.datepollsystems.waiterrobot.shared.core.di.injectLoggerForClass
import org.koin.core.component.KoinComponent

@SuppressLint("MissingPermission")
fun Terminal.discoverReaders(config: DiscoveryConfiguration): Flow<List<Reader>> {
    val discoverCallback = DiscoverCallback()
    this.discoverReaders(config, discoverCallback, discoverCallback)
    return discoverCallback.flow
}

private class DiscoverCallback : DiscoveryListener, Callback, KoinComponent {
    private val logger by injectLoggerForClass()

    private val _flow = MutableSharedFlow<List<Reader>>()
    val flow: Flow<List<Reader>> get() = _flow

    override fun onUpdateDiscoveredReaders(readers: List<Reader>): Unit = runBlocking {
        _flow.emit(readers)
    }

    override fun onFailure(e: TerminalException) {
        logger.e("Reader discovery failed", e)
    }

    override fun onSuccess() {
        logger.i("Reader discovery success")
    }
}
