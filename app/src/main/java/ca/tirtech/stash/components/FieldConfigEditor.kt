package ca.tirtech.stash.components

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import ca.tirtech.stash.R

class FieldConfigEditor(context: Context?) : LinearLayout(context) {
    init {
        View.inflate(context, R.layout.custom_field_config_editor, this)
    }
}
