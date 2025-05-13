package org.datepollsystems.waiterrobot.android.ui.core

import android.content.Context
import android.widget.Toast
import dev.icerock.moko.resources.desc.StringDesc

fun Context.toast(message: StringDesc, duration: Int) {
    Toast.makeText(this, message.toString(this), duration).show()
}
