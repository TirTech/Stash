package ca.tirtech.stash.util

import android.widget.EditText

fun EditText.value(): String {
    return this.text.toString()
}
