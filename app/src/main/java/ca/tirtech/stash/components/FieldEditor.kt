package ca.tirtech.stash.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import ca.tirtech.stash.R
import ca.tirtech.stash.database.entity.FieldConfig
import ca.tirtech.stash.database.types.FieldType
import ca.tirtech.stash.util.OnSelectedItemListenerImpl
import ca.tirtech.stash.util.setEntries
import ca.tirtech.stash.util.value
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText

class FieldEditor(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private val btnCancel: MaterialButton
    private val btnConfirm: MaterialButton
    private val txtFieldName: TextInputEditText
    private val spnFieldType: Spinner
    private val lblShow: SwitchMaterial
    private val fieldEntry: FieldEntry
    private val listEditor: ListBuilder

    private val config: FieldConfig = FieldConfig("Default", FieldType.STRING, null, false, "", ArrayList())
    var onCancelFunc: () -> Unit = {}
    var onConfirmFunc: (FieldConfig) -> Unit = {}

    init {
        val root = View.inflate(context, R.layout.custom_field_editor, this)
        fieldEntry = root.findViewById<FieldEntry>(R.id.fe_default).apply {
            setFieldConfig(config)
        }
        listEditor = root.findViewById<ListBuilder>(R.id.lstb_choices).apply {
            onChangeCallback = {
                config.choices = it
                fieldEntry.setFieldConfig(config)
            }
        }
        txtFieldName = root.findViewById<TextInputEditText>(R.id.edittxt_field_name)
        spnFieldType = root.findViewById<Spinner>(R.id.spn_field_type).apply {
            setEntries(FieldType.values())
            onItemSelectedListener =
                OnSelectedItemListenerImpl { adapterView: AdapterView<*>, _: View, i: Int ->
                    config.type = adapterView.adapter.getItem(i) as FieldType
                    fieldEntry.refresh()
                }
        }
        lblShow = root.findViewById<SwitchMaterial>(R.id.switch_show_label)
        btnCancel = root.findViewById<MaterialButton>(R.id.btn_cancel_field_add).apply {
            setOnClickListener { onCancelFunc() }
        }
        btnConfirm = root.findViewById<MaterialButton>(R.id.btn_confirm_field_add).apply {
            setOnClickListener {
                onConfirmFunc(config.apply {
                    name = txtFieldName.value()
                    showAsLabel = lblShow.isChecked
                    defaultValue = fieldEntry.getValue()
                })
            }
        }
    }
}
