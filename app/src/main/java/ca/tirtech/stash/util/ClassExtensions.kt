package ca.tirtech.stash.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.json.JSONArray
import org.json.JSONException
import java.util.*


fun <T> ArrayList<T>.fromJsonString(list: String): ArrayList<T> = apply {
    try {
        JSONArray(list).let { (0 until it.length()).forEach { i -> add(it.get(i) as T) } }
    } catch (e: JSONException) {
        e.printStackTrace()
    }
}

fun JSONArray.merge(other: JSONArray): JSONArray = apply {
    for (i in 0..length()) {
        put(other.get(i))
    }
}

fun <T> Collection<T>.toJsonString(): String = stream()
    .map { it.toString() }
    .collect({ JSONArray() },
             { ja, str -> ja.put(str) },
             { a, b -> a.merge(b) })
    .toString()

fun <T> Stack<T>.peekOrNull(): T? = try {
    peek()
} catch (e: Exception) {
    null
}

fun <T> Stack<T>.popOrNull(): T? = try {
    pop()
} catch (e: Exception) {
    null
}

/**
 * Pops items off of this stack until the top item satisfies `predicate`.
 *
 * @param predicate function accepting the top item. Returns whether to stop popping.
 * @param popMatch boolean dictating if the item that satisfied `predicate` is popped off the stack.
 * @return the item that satisfies `predicate`. Will also be the top item if `popMatch` is false.
 */
fun <T> Stack<T>.popTo(predicate: (T) -> Boolean, popMatch: Boolean = false): T? {
    while(peekOrNull()?.let{!predicate(it)} ?: false) {
        popOrNull()
    }
    return if (popMatch) popOrNull() else peekOrNull()
}

fun <T> LiveData<T>.observe(owner: LifecycleOwner, f: (T) -> Unit) {
    this.observe(owner, Observer {f(it)})
}


/**
 * Find the first element that matches `predicate` and return it's index or -1 if not found.
 *
 * @param T
 * @param predicate
 * @return
 */
inline fun <T> Iterable<T>.findIndex(predicate: (T) -> Boolean): Int {
    for ((index, element) in this.withIndex()) if (predicate(element)) return index
    return -1
}

/**
 * Return the first value of each pair as a list.
 *
 * @return list of first values
 */
fun <A,B,T: Pair<A,B>> Iterable<T>.firsts(): List<A>{
    return this.map { it.first }
}

/**
 * Return the second value of each pair as a list.
 *
 * @return list of second values
 */
fun <A,B,T: Pair<A,B>> Iterable<T>.seconds(): List<B>{
    return this.map { it.second }
}

/**
 * Automatically hide the keyboard when the focus is lost on this element.
 *
 */
fun EditText.autoHideKeyboard() {
    setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) getSystemService(this.context, InputMethodManager::class.java)?.hideSoftInputFromWindow(this.windowToken, 0)
    }
}
