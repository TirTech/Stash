package ca.tirtech.stash.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import ca.tirtech.stash.R
import ca.tirtech.stash.database.entity.FieldConfig
import ca.tirtech.stash.database.types.FieldType
import ca.tirtech.stash.databinding.CustomFieldEditorBinding
import ca.tirtech.stash.databinding.CustomFieldEntryBinding
import ca.tirtech.stash.util.value
import java.util.*

class FieldEntry(context: Context) : ConstraintLayout(context) {
    private var binding: CustomFieldEntryBinding

    init {
        binding = CustomFieldEntryBinding.inflate(LayoutInflater.from(context),this,false)
        binding.config = FieldConfig("Dummy",FieldType.STRING,null,false,"",ArrayList())
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
            FieldType.MULTI_CHOICE -> TODO()
            else -> ""
        }
    }
}
