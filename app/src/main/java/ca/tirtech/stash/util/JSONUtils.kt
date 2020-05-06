package ca.tirtech.stash.util;

import org.json.JSONArray
import org.json.JSONException
import java.util.*

fun <T> ArrayList<T>.fromJsonString(list: String): ArrayList<T> = apply {
    try {
        JSONArray(list).let { (0..it.length()).forEach { i -> add(it.get(i) as T) } };
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
