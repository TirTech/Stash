package ca.tirtech.stash.util

import androidx.databinding.ObservableList
import androidx.databinding.ObservableList.OnListChangedCallback

class ObservableListChangeListener<T : ObservableList<*>?>(val callback: (T) -> Unit) : OnListChangedCallback<T>() {
    override fun onChanged(sender: T) = callback(sender)
    override fun onItemRangeRemoved(sender: T, positionStart: Int, itemCount: Int) = callback(sender)
    override fun onItemRangeMoved(sender: T, fromPosition: Int, toPosition: Int, itemCount: Int) = callback(sender)
    override fun onItemRangeInserted(sender: T, positionStart: Int, itemCount: Int) = callback(sender)
    override fun onItemRangeChanged(sender: T, positionStart: Int, itemCount: Int) = callback(sender)
}
