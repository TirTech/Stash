package ca.tirtech.stash.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import ca.tirtech.stash.R
import ca.tirtech.stash.database.entity.FieldConfig
import ca.tirtech.stash.database.entity.FieldValue
import ca.tirtech.stash.database.types.FieldType
import ca.tirtech.stash.util.*
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class FieldEntry(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private val switchValue: SwitchMaterial
    private val textValue: TextInputEditText
    private val numValue: TextInputEditText
    private val spnSingle: Spinner
    private val msMulti: MultiSelect
    private var config: FieldConfig = FieldConfig("Dummy", FieldType.MULTI_CHOICE, null, false, "", arrayListOf("Test Z", "Test Y", "Test X"))
    private val textValueLayout: TextInputLayout
    private val numValueLayout: TextInputLayout
    private lateinit var editingValue: FieldValue


    init {
        val root = View.inflate(context, R.layout.custom_field_entry, this)
        switchValue = root.findViewById(R.id.switch_field_value)
        textValue = root.findViewById(R.id.edittxt_field_value)
        textValueLayout = root.findViewById(R.id.txtlayout_field_value)
        numValue = root.findViewById(R.id.editNum_field_value)
        numValueLayout = root.findViewById(R.id.numlayout_field_value)
        spnSingle = root.findViewById(R.id.spn_field_value)
        msMulti = root.findViewById(R.id.ms_field_value)
    }

    fun refresh() {
        switchValue.setVisibility(false)
        textValueLayout.setVisibility(false)
        numValueLayout.setVisibility(false)
        spnSingle.setVisibility(false)
        msMulti.setVisibility(false)

        when (config.type) {
            FieldType.STRING -> textValueLayout.apply {
                hint = config.name
                setVisibility(true)
                editText?.setText(editingValue.value)
            }
            FieldType.NUMBER -> numValueLayout.apply {
                hint = config.name
                setVisibility(true)
                editText?.setText(editingValue.value)
            }
            FieldType.BOOLEAN -> switchValue.apply {
                text = config.name
                setVisibility(true)
                isChecked = editingValue.value.toBoolean()
            }
            FieldType.SINGLE_CHOICE -> spnSingle.apply {
                setEntries(config.choices)
                setVisibility(true)
                setSelection(config.choices.indexOf(editingValue.value))
            }
            FieldType.MULTI_CHOICE -> msMulti.apply {
                setEntries(config.choices)
                setVisibility(true)
                setSelection(ArrayList<String>().fromJsonString(editingValue.value))
            }
        }
    }

    fun setFieldConfig(config: FieldConfig, value: FieldValue? = null) {
        this.config = config
        this.editingValue = value ?: FieldValue(config.id, config.defaultValue, -1)
        refresh()
    }

    fun getFieldConfig(): FieldConfig = this.config

    fun getValue(): FieldValue = editingValue.apply {
        value = when (config.type) {
            FieldType.STRING -> textValue.value()
            FieldType.NUMBER -> numValue.value()
            FieldType.BOOLEAN -> switchValue.isChecked.toString()
            FieldType.SINGLE_CHOICE -> spnSingle.selectedItem.toString()
            FieldType.MULTI_CHOICE -> msMulti.getValues().toJsonString()
        }
    }
}
