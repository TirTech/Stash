package ca.tirtech.stash.util

import android.view.View
import android.widget.AdapterView

class OnSelectedItemListenerImpl(val callback: (parent: AdapterView<*>, view: View, position: Int) -> Unit) :
    AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Do nothing
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        callback(parent, view, position)
    }

}
