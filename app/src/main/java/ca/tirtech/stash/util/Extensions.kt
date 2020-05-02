package ca.tirtech.stash.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.EditText
import androidx.lifecycle.LifecycleOwner

fun EditText.value(): String {
    return this.text.toString()
}

tailrec fun Context?.activity(): Activity? = when (this) {
    is Activity -> this
    else -> (this as? ContextWrapper)?.baseContext?.activity()
}

tailrec fun Context?.lifecycleOwner(): LifecycleOwner? = when (this) {
    is LifecycleOwner -> this
    else -> (this as? ContextWrapper)?.baseContext?.lifecycleOwner()
}
