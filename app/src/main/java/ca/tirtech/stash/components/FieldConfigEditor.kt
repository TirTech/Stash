package ca.tirtech.stash.components

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.tirtech.stash.R
import ca.tirtech.stash.database.entity.FieldConfig
import ca.tirtech.stash.databinding.ConfigRvCardBinding
import ca.tirtech.stash.util.MarginItemDecorator
import ca.tirtech.stash.util.ObservableListChangeListener
import ca.tirtech.stash.util.setVisibility
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar

class FieldConfigEditor(context: Context, attr: AttributeSet) : ConstraintLayout(context, attr) {
    private val rvConfigs: RecyclerView
    private val btnAdd: MaterialButton
    private val editor: FieldEditor
    private val adapter: ConfigAdapter

    init {
        adapter = ConfigAdapter(ObservableArrayList())

        val root = View.inflate(context, R.layout.custom_field_config_editor, this)
        rvConfigs = root.findViewById<RecyclerView>(R.id.rv_fields).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@FieldConfigEditor.adapter
            addItemDecoration(MarginItemDecorator(5))
        }
        editor = root.findViewById<FieldEditor>(R.id.fed_category_field_editor).apply {
            onCancelFunc = { setEditorVisible(false) }
            onConfirmFunc = {
                adapter.configs.add(it)
                Log.i("FieldConfigEditor", "Configuration was $it")
                setEditorVisible(false)
            }
        }
        btnAdd = root.findViewById<MaterialButton>(R.id.btn_add_new_field).also {
            it.setOnClickListener { setEditorVisible(true) }
        }
        setEditorVisible(false)
    }

    fun getConfigs() = adapter.configs.toList()

    fun setConfigs(configs: List<FieldConfig>) {
        adapter.configs.clear()
        adapter.configs.addAll(configs)
    }

    private fun setEditorVisible(visible: Boolean) {
        editor.setVisibility(visible)
        rvConfigs.setVisibility(!visible)
        btnAdd.setVisibility(!visible)
    }

    inner class ConfigAdapter(val configs: ObservableArrayList<FieldConfig>) :
        RecyclerView.Adapter<ConfigAdapter.ConfigViewHolder>() {

        inner class ConfigViewHolder(var binding: ConfigRvCardBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfigViewHolder = ConfigViewHolder(
            ConfigRvCardBinding.inflate(LayoutInflater.from(context), parent, false)
        )

        override fun onBindViewHolder(holder: ConfigViewHolder, position: Int) {
            val data = configs[position]
            holder.binding.apply {
                title = data.name
                description = data.type.toString()
                root.setOnClickListener {
                    Snackbar.make(root, R.string.not_implemented, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        override fun getItemCount(): Int = configs.size

        init {
            this.configs.addOnListChangedCallback(ObservableListChangeListener { notifyDataSetChanged() })
        }
    }
}
