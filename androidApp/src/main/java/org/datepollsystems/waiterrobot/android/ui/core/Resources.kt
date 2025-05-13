package org.datepollsystems.waiterrobot.android.ui.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.StringDesc

@Composable
@ReadOnlyComposable
operator fun StringResource.invoke() = stringResource(this.resourceId)

@Composable
@ReadOnlyComposable
operator fun StringResource.invoke(vararg formatArgs: Any) =
    stringResource(this.resourceId, *formatArgs)

@Composable
@ReadOnlyComposable
operator fun StringDesc.invoke(): String = this.toString(LocalContext.current)
