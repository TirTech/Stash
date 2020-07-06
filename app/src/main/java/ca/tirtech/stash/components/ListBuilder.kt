package ca.tirtech.stash.components

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import ca.tirtech.stash.R
import ca.tirtech.stash.util.autoHideKeyboard
import ca.tirtech.stash.util.clear
import ca.tirtech.stash.util.value
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText

class ListBuilder(context: Context?, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {
    constructor(context: Context?) : this(context, null)

    private val chips: HashMap<Chip, String> = HashMap()
    private val txtValue: TextInputEditText
    private val btnAdd: MaterialButton
    private val chipGroup: ChipGroup
    var onChangeCallback: (ArrayList<String>) -> Unit = {}

    init {
        val root = inflate(context, R.layout.custom_list_editor, this)
        txtValue = root.findViewById<TextInputEditText>(R.id.txt_lst_edit_value).apply {
            autoHideKeyboard()
        }
        btnAdd = root.findViewById(R.id.btn_lst_editor_add)
        chipGroup = root.findViewById(R.id.chpgrp_lst_editor)
        btnAdd.setOnClickListener { addChip() }
    }

    fun getValues(): ArrayList<String> = ArrayList(chips.values)

    private fun addChip() = Chip(context).also { chip ->
        val data = txtValue.value()
        txtValue.clear()
        chip.text = data
        chip.isCloseIconVisible = true
        chip.setOnCloseIconClickListener {
            chips.remove(chip)
            chipGroup.removeView(chip)
            onChangeCallback(getValues())
        }
        chips[chip] = data
        chipGroup.addView(chip)
        onChangeCallback(getValues())
    }
}
