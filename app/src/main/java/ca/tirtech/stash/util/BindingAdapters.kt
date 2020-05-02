package ca.tirtech.stash.util

import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import ca.tirtech.stash.R

@BindingAdapter("android:entries")
fun Spinner.setEntries(entries: List<Any>) {
    adapter = ArrayAdapter(context,
                           R.layout.support_simple_spinner_dropdown_item,
                           entries).apply { setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item) }
}

@BindingAdapter("android:entries")
fun Spinner.setEntries(entries: Array<Any>) {
    adapter = ArrayAdapter(context,
                           R.layout.support_simple_spinner_dropdown_item,
                           entries).apply { setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item) }
}

@BindingAdapter("android:visibility")
fun View.setVisibility(value: Boolean) {
    visibility = if (value) View.VISIBLE else View.GONE
}
