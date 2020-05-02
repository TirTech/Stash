package ca.tirtech.stash.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.tirtech.stash.NewCategoryModel
import ca.tirtech.stash.R
import ca.tirtech.stash.database.entity.FieldConfig
import ca.tirtech.stash.databinding.ConfigRvCardBinding
import ca.tirtech.stash.databinding.CustomFieldConfigEditorBinding
import ca.tirtech.stash.util.DeepLiveData
import ca.tirtech.stash.util.MarginItemDecorator
import ca.tirtech.stash.util.activity
import ca.tirtech.stash.util.lifecycleOwner
import com.google.android.material.snackbar.Snackbar

class FieldConfigEditor(context: Context, attr: AttributeSet) : ConstraintLayout(context, attr) {
    private var binding: CustomFieldConfigEditorBinding
    private var model: NewCategoryModel

    init {
        val activity = context.activity() as AppCompatActivity
        model = ViewModelProvider(activity).get(NewCategoryModel::class.java)
        val adapter = ConfigAdapter(model.fieldConfigs, activity)

        binding = CustomFieldConfigEditorBinding.inflate(LayoutInflater.from(context), this, true).also {
            it.lifecycleOwner = activity
            it.rvFields.also { rv ->
                rv.layoutManager = LinearLayoutManager(context)
                rv.adapter = adapter
                rv.addItemDecoration(MarginItemDecorator(5))
            }
        }
    }

    inner class ConfigAdapter(configs: DeepLiveData<ObservableArrayList<FieldConfig>>, viewLifecycleOwner: LifecycleOwner) :
        RecyclerView.Adapter<ConfigAdapter.ConfigViewHolder>() {
        val configs: LiveData<ObservableArrayList<FieldConfig>>

        inner class ConfigViewHolder(var binding: ConfigRvCardBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfigViewHolder = ConfigViewHolder(
            ConfigRvCardBinding.inflate(LayoutInflater.from(context), parent, false)
        )

        override fun onBindViewHolder(holder: ConfigViewHolder, position: Int) {
            val data = configs.value!![position]
            holder.binding.apply {
                title = data.name
                description = data.type.toString()
                root.setOnClickListener {
                    Snackbar.make(root, R.string.not_implemented, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        override fun getItemCount(): Int = if (configs.value == null) 0 else configs.value!!.size

        init {
            this.configs = Transformations.map(configs) { configList: ObservableArrayList<FieldConfig> -> configList }.apply {
                observe(viewLifecycleOwner, Observer { notifyDataSetChanged() })
            }
        }
    }
}
