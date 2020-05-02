package ca.tirtech.stash.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import ca.tirtech.stash.database.entity.FieldConfig
import ca.tirtech.stash.database.types.FieldType
import ca.tirtech.stash.databinding.CustomFieldEntryBinding
import ca.tirtech.stash.util.toJsonString
import ca.tirtech.stash.util.value

class FieldEntry(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private var binding: CustomFieldEntryBinding

    init {
        binding = CustomFieldEntryBinding.inflate(LayoutInflater.from(context), this, true).apply {
            config = FieldConfig("Dummy", FieldType.MULTI_CHOICE, null, false, "", arrayListOf("Test Z","Test Y", "Test X"))
        }
    }

    fun setFieldConfig(config: FieldConfig) {
        binding.config = config
        binding.invalidateAll()
    }

    fun getValue(): String {
        return when (binding.config ?: FieldType.STRING) {
            FieldType.STRING -> binding.edittxtFieldValue.value()
            FieldType.NUMBER -> binding.editNumFieldValue.value()
            FieldType.BOOLEAN -> binding.switchFieldValue.isChecked.toString()
            FieldType.SINGLE_CHOICE -> binding.spnFieldValue.selectedItem.toString()
            FieldType.MULTI_CHOICE -> binding.msFieldValue.getValues().toJsonString()
            else -> ""
        }
    }
}
