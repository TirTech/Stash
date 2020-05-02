package ca.tirtech.stash.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import ca.tirtech.stash.databinding.CustomFieldEditorBinding

class FieldEditor(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private var binding: CustomFieldEditorBinding

    init {
        binding = CustomFieldEditorBinding.inflate(LayoutInflater.from(context), this, true).apply {}
    }
}
