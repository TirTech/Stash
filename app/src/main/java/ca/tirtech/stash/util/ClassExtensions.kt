package ca.tirtech.stash.util;

import org.json.JSONArray
import org.json.JSONException
import java.util.*

fun <T> ArrayList<T>.fromJsonString(list: String): ArrayList<T> = apply {
    try {
        JSONArray(list).let { (0 until it.length()).forEach { i -> add(it.get(i) as T) } };
    } catch (e: JSONException) {
        e.printStackTrace();
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
