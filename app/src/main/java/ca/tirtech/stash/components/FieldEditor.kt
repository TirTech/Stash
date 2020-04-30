package ca.tirtech.stash.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import ca.tirtech.stash.R
import ca.tirtech.stash.database.types.FieldType
import ca.tirtech.stash.databinding.CustomFieldEditorBinding

class FieldEditor(context: Context) : LinearLayout(context) {
    private var binding: CustomFieldEditorBinding

    init {
        binding = CustomFieldEditorBinding.inflate(LayoutInflater.from(context),this,false)
    }
}
