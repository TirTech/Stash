package ca.tirtech.stash.components

import android.content.Context
import android.util.AttributeSet
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import ca.tirtech.stash.R
import ca.tirtech.stash.util.setEntries
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class MultiSelect(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private val chips: HashMap<Chip, Any> = HashMap()
    private val spinner: Spinner
    private val btnAdd: MaterialButton
    private val chipGroup: ChipGroup

    init {
        val root = inflate(context, R.layout.custom_multi_select, this)
        spinner = root.findViewById(R.id.spn_multiselect)
        btnAdd = root.findViewById(R.id.btn_multiselect)
        chipGroup = root.findViewById(R.id.chpgrp_multiselect)
        btnAdd.setOnClickListener { addChip() }
        if (isInEditMode) {
            setEntries(resources.getStringArray(R.array.test_strings).toList())
        }
    }

    fun getValues(): ArrayList<Any> = arrayListOf(chips.values)

    fun setEntries(lst: List<Any>) {
        spinner.setEntries(lst)
    }

    private fun addChip() = Chip(context).also { chip ->
        chip.text = spinner.selectedItem.toString()
        chip.isCloseIconVisible = true
        chip.setOnCloseIconClickListener {
            chips.remove(chip)
            chipGroup.removeView(chip)
        }
        chips[chip] = spinner.selectedItem
        chipGroup.addView(chip)
    }
}
