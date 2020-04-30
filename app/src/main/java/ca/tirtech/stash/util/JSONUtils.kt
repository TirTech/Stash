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
