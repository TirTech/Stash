package ca.tirtech.stash.forms

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout

abstract class FormControl(field: FormField, context: Context): ConstraintLayout(context) {
    abstract fun getValue(): Any
    abstract fun onDefaultsReady(value: Any?)
    open fun onFormCancel() {}
}
