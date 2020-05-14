package ca.tirtech.stash.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.google.android.material.button.MaterialButton

fun EditText.value(): String {
    return this.text.toString()
}

/**
 * Assign a navigation destination to be navigated to when this button is clicked. If `action` is null, the controller will
 * pop the back stack instead of performing an action. This function returns the receiver to allow for chaining.
 *
 * @param controller the controller to navigate using
 * @param action the action to perform, or null to pop back stack
 * @return `receiver`
 */
fun MaterialButton.navigateOnClick(controller: NavController, @IdRes action: Int? = null): MaterialButton = apply {
    setOnClickListener {
        if (action != null) controller.navigate(action) else controller.popBackStack()
    }
}

tailrec fun Context?.activity(): Activity? = when (this) {
    is Activity -> this
    else -> (this as? ContextWrapper)?.baseContext?.activity()
}

tailrec fun Context?.lifecycleOwner(): LifecycleOwner? = when (this) {
    is LifecycleOwner -> this
    else -> (this as? ContextWrapper)?.baseContext?.lifecycleOwner()
}
