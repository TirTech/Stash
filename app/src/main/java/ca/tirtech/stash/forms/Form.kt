package ca.tirtech.stash.forms

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import ca.tirtech.stash.R
import com.google.android.material.button.MaterialButton
import kotlin.system.measureTimeMillis

class Form(val name: String, context: Context, onCancel: () -> Unit = {}, onSubmitComplete: (Any) -> Unit = {}, val onSubmit: ()-> Any = {}) {

    val root: ScrollView = LayoutInflater.from(context).inflate(R.layout.custom_form, null, false) as ScrollView
    private val rootForm: Subform = root.findViewById<Subform>(R.id.form_root_subform).apply {
        name = this@Form.name
        onSubmit = {_:Any -> this@Form.onSubmit()}
    }

    constructor(
        @StringRes name: Int,
        context: Context,
        onCancel: () -> Unit = {},
        onSubmitComplete: (Any) -> Unit = {},
        onSubmit: ()-> Any = {}
    ) : this(context.resources.getString(name), context, onCancel, onSubmitComplete, onSubmit)

    init {
        root.findViewById<MaterialButton>(R.id.btn_submit_form).setOnClickListener { onSubmitComplete(rootForm.submit(Unit)) }
        root.findViewById<MaterialButton>(R.id.btn_cancel_form).setOnClickListener {
            rootForm.cancel()
            onCancel()
        }
    }

    fun addForm(form: Subform): Form = this.apply {
        rootForm.addForm(form)
    }

    fun addField(
        @StringRes name: Int,
        type: String,
        configs: Map<String, *>? = null,
        onSubmit: Accumulator<Any> = { _, _ ->},
        default: suspend () -> Any? = { null }
    ): Form = this.apply {
        rootForm.addField(name, type, configs, onSubmit, default)
    }

    fun addField(
        name: String,
        type: String,
        configs: Map<String, *>? = null,
        onSubmit: Accumulator<Any> = { _, _ ->},
        default: suspend () -> Any? = { null }
    ): Form = this.apply {
        rootForm.addField(name, type, configs, onSubmit, default)
    }

    fun build(): Form = this.apply {
        Log.i("Form", "Form took ${measureTimeMillis { rootForm.build() }}ms to build")
    }

    fun mount(view: ViewGroup) {
        view.addView(
            root,
            ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        )
    }
}

class Subform(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    //Pending
    private val fields = ArrayList<FormField>()
    private val subforms = ArrayList<Subform>()

    //Created
    private val createdFields = ArrayList<Pair<FormField, FormControl>>()
    private val createdSubforms = ArrayList<Subform>()

    //View Roots
    private val root: ConstraintLayout = (LayoutInflater.from(context).inflate(R.layout.custom_subform, null, false) as ConstraintLayout).also {
        this.addView(it, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
    }
    private val fieldContainer: LinearLayout = root.findViewById(R.id.form_field_container)
    private val subformContainer: LinearLayout = root.findViewById(R.id.form_subform_container)
    private val tvTitle: TextView = root.findViewById(R.id.tv_subform_title)

    var name: String
        get() = tvTitle.text.toString()
        set(v) {
            tvTitle.text = v
        }

    var onSubmit: (Any) -> Any = {}

    constructor(name: String, context: Context, onSubmit: (Any) -> Any = {a -> a}, attrs: AttributeSet? = null): this(context,attrs) {
        this.name = name
        this.onSubmit = onSubmit
    }

    constructor(
        @StringRes name: Int,
        context: Context,
        onSubmit: (Any) -> Any = {a -> a},
        attrs: AttributeSet? = null
    ) : this(context.resources.getString(name), context, onSubmit, attrs)

    fun addForm(form: Subform): Subform = this.apply {
        subforms.add(form)
    }

    fun addField(
        @StringRes name: Int,
        type: String,
        configs: Map<String, *>? = null,
        onSubmit: Accumulator<Any> = { _, _ ->},
        default: suspend () -> Any? = { null }
    ): Subform =
        addField(root.context.resources.getString(name), type, configs, onSubmit, default)

    fun addField(
        name: String,
        type: String,
        configs: Map<String, *>? = null,
        onSubmit: Accumulator<Any> = { _, _ ->},
        default: suspend () -> Any? = { null }
    ): Subform = this.apply {
        if (!FormControlRegistry.checkTypeRegistered(type)) {
            throw UnsupportedFieldTypeException("The field type $type is not registered")
        }
        fields.add(FormField(name, type, configs ?: HashMap<String, Any>(), onSubmit = onSubmit, defaultProvider = default))
    }

    /**
     * Build or update the form UI. This will recursively build all nested forms.
     */
    fun build() {
        val context = root.context
        createdSubforms.forEach { it.build() }

        fields.map { v ->
            v to FormControlRegistry.makeControlForField(context, v, fieldContainer)
        }.toCollection(createdFields)

        subforms.onEach {
            it.build()
            subformContainer.addView(it)
        }.toCollection(createdSubforms)

        fields.clear()
        subforms.clear()
    }

    fun submit(acc: Any): Any {
        val res = onSubmit(acc)
        createdFields.forEach { it.first.onSubmit(it.second.getValue(), res)}
        createdSubforms.forEach { it.submit(res) }
        return res
    }

    fun cancel() {
        createdFields.forEach { it.second.onFormCancel() }
        createdSubforms.forEach { it.cancel() }
    }
}
