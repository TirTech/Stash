package ca.tirtech.stash.forms

import android.content.Context
import android.view.View
import ca.tirtech.stash.R
import ca.tirtech.stash.components.MaterialSpinner
import ca.tirtech.stash.components.MultiSelect
import ca.tirtech.stash.util.autoHideKeyboard
import ca.tirtech.stash.util.setVisibility
import ca.tirtech.stash.util.value
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MultiFormControl(val field: FormField, context: Context) : FormControl(field, context) {

    private val switchValue: SwitchMaterial
    private val txtValue: TextInputEditText
    private val numValue: TextInputEditText
    private val spnSingle: MaterialSpinner
    private val msMulti: MultiSelect
    private val textValueLayout: TextInputLayout
    private val numValueLayout: TextInputLayout

    init {
        val root = View.inflate(context, R.layout.custom_field_entry, this)
        switchValue = root.findViewById(R.id.switch_field_value)
        txtValue = root.findViewById<TextInputEditText>(R.id.edittxt_field_value).apply {
            autoHideKeyboard()
        }
        textValueLayout = root.findViewById(R.id.txtlayout_field_value)
        numValue = root.findViewById<TextInputEditText>(R.id.editNum_field_value).apply {
            autoHideKeyboard()
        }
        numValueLayout = root.findViewById(R.id.numlayout_field_value)
        spnSingle = root.findViewById(R.id.spn_field_value)
        msMulti = root.findViewById(R.id.ms_field_value)

        switchValue.setVisibility(false)
        textValueLayout.setVisibility(false)
        numValueLayout.setVisibility(false)
        spnSingle.setVisibility(false)
        msMulti.setVisibility(false)

        when (field.type) {
            FormControlRegistry.Control.STRING -> textValueLayout.apply {
                hint = field.name
                setVisibility(true)
            }
            FormControlRegistry.Control.INT -> numValueLayout.apply {
                hint = field.name
                setVisibility(true)
            }
            FormControlRegistry.Control.BOOLEAN -> switchValue.apply {
                text = field.name
                setVisibility(true)
            }
            FormControlRegistry.Control.SINGLE_CHOICE -> spnSingle.apply {
                entries = field.config["options"] as ArrayList<String>
                setVisibility(true)
            }
            FormControlRegistry.Control.MULTI_CHOICE -> msMulti.apply {
                setEntries(field.config["options"] as ArrayList<String>)
                setVisibility(true)
            }
            else -> throw InvalidTypeException("The field type ${field.type} is not supported for this control")
        }
    }

    override fun getValue(): Any = when (field.type) {
        FormControlRegistry.Control.STRING -> txtValue.value()
        FormControlRegistry.Control.INT -> numValue.value()
        FormControlRegistry.Control.BOOLEAN -> switchValue.isChecked.toString()
        FormControlRegistry.Control.SINGLE_CHOICE -> spnSingle.selectedItem
        FormControlRegistry.Control.MULTI_CHOICE -> msMulti.getValues()
        else -> throw InvalidTypeException("The field type ${field.type} is not supported for this control")
    }

    override fun onDefaultsReady(value: Any?) {
        if (value != null) {
            when (field.type) {
                FormControlRegistry.Control.STRING -> textValueLayout.apply {
                    editText?.setText(value as String)
                }
                FormControlRegistry.Control.INT -> numValueLayout.apply {
                    editText?.setText(value as Int)
                }
                FormControlRegistry.Control.BOOLEAN -> switchValue.apply {
                    isChecked = value as Boolean
                }
                FormControlRegistry.Control.SINGLE_CHOICE -> spnSingle.apply {
                    setSelection(entries.indexOf(value as String))
                }
                FormControlRegistry.Control.MULTI_CHOICE -> msMulti.apply {
                    setSelection(value as ArrayList<String>)
                }
            }
        }
    }
}
