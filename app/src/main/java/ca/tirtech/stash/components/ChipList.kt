package ca.tirtech.stash.components

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ChipList<T : Any>(context: Context?, attrs: AttributeSet?) : ChipGroup(context, attrs) {
    constructor(context: Context?) : this(context, null)

    private var items: ArrayList<T> = ArrayList()
    private var chips: ArrayList<Pair<Chip, T>> = ArrayList()
    var chipBuilder: (T, Chip) -> Unit = { i, c ->
        c.text = i.toString()
    }

    fun setItems(items: List<T>) {
        this.items.clear()
        addAllItems(items)
        buildChips()
    }

    @Suppress("UNCHECKED_CAST")
    fun getItems(): List<T> = this.items.clone() as List<T>

    fun addAllItems(items: List<T>) {
        this.items.addAll(items)
        buildChips()
    }

    fun addItem(item: T) {
        this.items.add(item)
        buildChips()
    }

    fun removeItem(item: T) {
        this.items.remove(item)
        buildChips()
    }

    private fun buildChips() {
        // For each chip, validate there is an item; for each item, validate that there is a chip
        items.filterNot { i ->
            chips.any { p -> p.second == i }
        }.forEach {
            chips.add(
                Chip(context).also { c ->
                    chipBuilder(it, c)
                    this.addView(c)
                } to it
            )
        }

        val toRemove = chips.filterNot { items.contains(it.second) }
            .onEach { this.removeView(it.first) }
        chips.removeAll(toRemove)
    }
}
