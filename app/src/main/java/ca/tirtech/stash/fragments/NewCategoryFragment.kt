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
import ca.tirtech.stash.components.FieldConfigEditor
import ca.tirtech.stash.database.AppDatabase.Companion.db
import ca.tirtech.stash.database.entity.Category
import ca.tirtech.stash.database.entity.CategoryWithFieldConfigs
import ca.tirtech.stash.database.repositories.Repository
import ca.tirtech.stash.util.autoHideKeyboard
import ca.tirtech.stash.util.navigateOnClick
import ca.tirtech.stash.util.value
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewCategoryFragment : Fragment() {
    private lateinit var btnCancel: MaterialButton
    private lateinit var btnSave: MaterialButton
    private lateinit var txtCategoryName: TextInputEditText
    private lateinit var fieldConfigEditor: FieldConfigEditor
    private lateinit var model: CollectionModel
    private lateinit var navController: NavController
    private var editingCategory: CategoryWithFieldConfigs? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        model = ViewModelProvider(requireActivity()).get(CollectionModel::class.java)
        navController = Navigation.findNavController(container!!)
        val root = inflater.inflate(R.layout.fragment_new_category, container, false)
        btnCancel = root.findViewById<MaterialButton>(R.id.btn_cancel_new_category).navigateOnClick(navController)
        btnSave = root.findViewById<MaterialButton>(R.id.btn_apply_new_category).apply {
            setOnClickListener(this@NewCategoryFragment::handleSaveClicked)
        }
        txtCategoryName = root.findViewById<TextInputEditText>(R.id.edittxt_category_name).apply {
            autoHideKeyboard()
        }
        fieldConfigEditor = root.findViewById(R.id.fieldConfigEditor)

        val editId = arguments?.getInt(CATEGORY_ID)

        if (editId != null) {
            CoroutineScope(Dispatchers.Main).launch {
                db.categoryDAO().getCategoryWithFieldConfigs(editId)?.also {
                    editingCategory = it
                    txtCategoryName.setText(it.category.name)
                    fieldConfigEditor.setConfigs(it.fieldConfigs)
                }
            }
        }

        return root
    }

    fun handleSaveClicked(view: View) {
        val name = txtCategoryName.value()
        if (name.isNotEmpty()) {
            if (editingCategory == null) {
                CoroutineScope(Dispatchers.Default).launch {
                    Repository.createCategoryWithFields(
                    Category(name, model.currentCategory.value!!.category.id),
                    fieldConfigEditor.getConfigs()
                )}.invokeOnCompletion {
                    model.refreshCategory()
                }
            } else {
                editingCategory?.also { ei ->
                    ei.category.name = name
                    CoroutineScope(Dispatchers.Default).launch {
                        Repository.updateCategoryWithFieldConfigs(ei.category, fieldConfigEditor.getConfigs())
                    }.invokeOnCompletion {
                        model.refreshCategory()
                    }
                }
            }
            navController.popBackStack()
        } else {
            Snackbar.make(view, R.string.new_category_name_invalid, Snackbar.LENGTH_LONG).show()
        }
    }

    companion object {
        const val CATEGORY_ID = "CATEGORY_ID"
    }
}
