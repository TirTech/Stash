package ca.tirtech.stash.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.google.android.material.button.MaterialButton

/**
 * Get the value of this text field
 *
 * @return the field's value
 */
fun EditText.value(): String {
    return this.text.toString()
}

/**
 * Clear the value of this text field
 *
 */
fun EditText.clear() {
    this.text.clear()
}

fun Spinner.getEntries(): List<*> = adapter.run {
    if (this is ArrayAdapter<*>) this.getEntries() else ArrayList<Any>()
}

fun <T:Any> ArrayAdapter<T>.getEntries(): List<T> = (0 until count).mapNotNull { getItem(it) }

/**
 * Assign a navigation destination to be navigated to when this button is clicked. If `action` is null, the controller will
 * pop the back stack instead of performing an action. This function returns the receiver to allow for chaining.
 *
 * @param controller the controller to navigate using
 * @param action the action to perform, or null to pop back stack
 * @return `receiver`
 */
fun MaterialButton.navigateOnClick(
    controller: NavController,
    @IdRes action: Int? = null,
    func: ((View) -> Unit)? = null
): MaterialButton = apply {
    setOnClickListener {
        if (func != null) func(it)
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

/**
 * Get the lifecycle coroutine scope for this fragment.
 *
 * @return the scope for this fragment's lifecycle
 */
fun Fragment.lifecycleScope(): LifecycleCoroutineScope {
    return viewLifecycleOwner.lifecycleScope
}

fun ImageView.loadFromFile(file: String, adjBounds: Boolean = true) {
    val bm = BitmapFactory.decodeFile(file)
    adjustViewBounds = adjBounds
    setImageBitmap(bm)
}
