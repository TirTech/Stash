package ca.tirtech.stash.forms

import android.content.Context
import android.view.ViewGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object FormControlRegistry {

    object Control {
        const val STRING ="String"
        const val INT = "Int"
        const val BOOLEAN = "Boolean"
        const val SINGLE_CHOICE = "Single_Choice"
        const val MULTI_CHOICE = "Multi_Choice"
        const val PHOTO = "Photo"
    }

    private val controls = HashMap<String,KClass<out FormControl>>()

    fun registerControl(type: String, control: KClass<out FormControl>) {
        controls[type] = control
    }

    /**
     * Checks that there is a registered control for the provided field type class.
     *
     * @return true if a control exists
     */
    fun checkTypeRegistered(type: String): Boolean {
        return controls.containsKey(type)
    }

    /**
     * Construct a FormControl from a FormField based on the registered controls
     *
     * @param context
     * @param field
     * @return
     */
    fun makeControlForField(context: Context, field: FormField, parent: ViewGroup): FormControl {
        val constructor = controls[field.type]?.primaryConstructor
            ?: throw UnsupportedFieldTypeException("The field's type was not registered or the class had no constructor of the correct type")

        val obj = if (constructor.parameters[0].type is Context)
            constructor.call(context, field)
        else
            constructor.call(field, context)

        parent.addView(obj)
        CoroutineScope(Dispatchers.Default).launch {
            field.defaultProvider?.let {
                val res = it().takeUnless { it is Unit }
                withContext(Dispatchers.Main) {
                    obj.onDefaultsReady(res)
                }
            }
        }
        return obj
    }
}
