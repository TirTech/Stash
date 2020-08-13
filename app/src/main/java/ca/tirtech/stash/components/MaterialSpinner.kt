package ca.tirtech.stash.components

import android.content.Context
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.constraintlayout.widget.ConstraintLayout
import ca.tirtech.stash.R

class MaterialSpinner(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private val actv: AutoCompleteTextView
    var entries = ArrayList<String>().apply { addAll(listOf("TestA","TestB","TestC")) }
        set(value) {
            field.clear()
            field.addAll(value)
            adapter.notifyDataSetChanged()
        }
    private val adapter = ArrayAdapter(context, R.layout.custom_material_spinner_item, entries)
    val selectedItem: String
        get() {
            return actv.text.toString()
        }

    init {
        val root = inflate(context, R.layout.custom_material_spinner, this)
        actv = root.findViewById(R.id.mtspn_actv)
        actv.inputType = 0
        actv.setAdapter(adapter)
    }

    fun setSelection(index: Int) {
        if (entries.size > 0) {
            actv.clearComposingText()
            actv.setText(entries[index.coerceIn(0, entries.size - 1)], false)
        } else {
            actv.clearComposingText()
            actv.setText("", false)
        }
    }

}
