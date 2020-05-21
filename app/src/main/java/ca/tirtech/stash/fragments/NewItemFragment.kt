package ca.tirtech.stash.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import ca.tirtech.stash.CollectionModel
import ca.tirtech.stash.R
import ca.tirtech.stash.components.FieldEntry
import ca.tirtech.stash.database.AppDatabase.Companion.db
import ca.tirtech.stash.database.entity.FieldConfig
import ca.tirtech.stash.database.entity.FieldValue
import ca.tirtech.stash.database.entity.Item
import ca.tirtech.stash.database.entity.ItemWithFieldValuesAndConfigs
import ca.tirtech.stash.database.repositories.Repository
import ca.tirtech.stash.util.navigateOnClick
import ca.tirtech.stash.util.value
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewItemFragment : Fragment() {
    private lateinit var btnCancel: MaterialButton
    private lateinit var btnSave: MaterialButton
    private lateinit var model: CollectionModel
    private lateinit var navController: NavController
    private lateinit var itemTitle: TextInputEditText
    private lateinit var itemDescription: TextInputEditText
    private var fieldEntries: ArrayList<FieldEntry> = ArrayList()
    private lateinit var entryContainer: ViewGroup
    private var editingItem: ItemWithFieldValuesAndConfigs? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        navController = Navigation.findNavController(container!!)
        model = ViewModelProvider(requireActivity()).get(CollectionModel::class.java)
        val root = inflater.inflate(R.layout.fragment_new_item, container, false)
        itemTitle = root.findViewById(R.id.edittxt_item_title)
        itemDescription = root.findViewById(R.id.edittxt_item_description)
        btnCancel = root.findViewById<MaterialButton>(R.id.btn_cancel_new_item).navigateOnClick(navController)
        btnSave = root.findViewById<MaterialButton>(R.id.btn_apply_new_item).apply {
            setOnClickListener(this@NewItemFragment::handleSaveClicked)
        }
        entryContainer = root.findViewById(R.id.editor_container)

        val editId = arguments?.getInt(ITEM_ID)

        CoroutineScope(Dispatchers.Main).launch {
            if (editId == null) {
                Repository.getCategoryFieldConfigsForItem(model.currentCategory.value!!.category.id).forEach { addEditor(it) }
            } else {
                db.itemDAO().getItemWithFieldValuesAndConfigs(editId)?.also {
                    this@NewItemFragment.editingItem = it
                    itemTitle.setText(it.item.title)
                    itemDescription.setText(it.item.description)
                    it.fieldValues.forEach { fv -> addEditor(fv.fieldConfig, fv.fieldValue) }
                }
            }
        }
        return root
    }

    fun handleSaveClicked(view: View) {
        val title = itemTitle.value()
        val description = itemDescription.value()
        if (title.isNotEmpty()) {
            if (description.isNotEmpty()) {
                if (editingItem == null) {
                    Repository.createItemWithFields(
                        Item(model.currentCategory.value!!.category.id, title, description),
                        fieldEntries.map { it.getValue() }
                    )
                } else {
                    editingItem?.also { ei ->
                        ei.item.title = title
                        ei.item.description = description
                        Repository.updateItemWithFields(ei.item, fieldEntries.map {it.getValue()})
                    }
                }
                navController.popBackStack()
            } else Snackbar.make(view, R.string.new_item_description_invalid, Snackbar.LENGTH_LONG).show()
        } else Snackbar.make(view, R.string.new_item_title_invalid, Snackbar.LENGTH_LONG).show()
    }

    private fun addEditor(config: FieldConfig, fieldValue: FieldValue? = null) {
        val entry = FieldEntry(context!!,null)
        entryContainer.addView(entry,ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        entry.setPadding(0,0,0,resources.getDimensionPixelSize(R.dimen.item_spacing))
        entry.setFieldConfig(config, fieldValue)
        fieldEntries.add(entry)
    }

    companion object {
        const val ITEM_ID = "ITEM_ID"
    }
}
